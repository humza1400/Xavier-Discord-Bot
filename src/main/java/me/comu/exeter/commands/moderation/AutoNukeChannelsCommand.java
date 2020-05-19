package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class AutoNukeChannelsCommand implements ICommand {

    private boolean isRunning = false;
    private long delay = 1;
    private final List<String> ancChannels = new ArrayList<>();
    private ScheduledExecutorService anc;
    private ScheduledFuture<?> scheduledFuture;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && Objects.requireNonNull(event.getMember()).getIdLong() != 699562509366984784L) {
            event.getChannel().sendMessage("You don't have permission to interact with the the ANC").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("I don't have permissions to start the ANC").queue();
            return;
        }


        Thread thread = new Thread(() -> {
            for (String s : ancChannels) {
                try {
                    TextChannel textChannel = event.getJDA().getTextChannelById(s);
                    if (textChannel != null) {
                        textChannel.createCopy().setNSFW(textChannel.isNSFW()).setSlowmode(textChannel.getSlowmode()).setParent(textChannel.getParent()).setPosition(textChannel.getPosition()).queue((textChannel1 -> {
                            ancChannels.remove(textChannel.getId());
                            textChannel.delete().queue();
                            textChannel1.sendMessage("Channel Auto Nuked! Current Delay: `" + delay + "` hours").queue();
                            ancChannels.add(textChannel1.getId());
                        }));
                    }
                } catch (NullPointerException ex) {
                    ancChannels.remove(s);
                }
            }
        });

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to set this channel as ANC").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.getChannel().sendMessage("I don't have permissions to set this channel as ANC").queue();
            return;
        }
        if (args.isEmpty()) {
            if (ancChannels.contains(event.getChannel().getId())) {
                event.getChannel().sendMessage("Removed `" + event.getChannel().getName() + "` from the ANC hash").queue();
                ancChannels.remove(event.getChannel().getId());
                return;
            }
            event.getChannel().sendMessage("`" + event.getChannel().getName() + "` will now be auto-nuked every " + delay + " hours").queue();
            ancChannels.add(event.getChannel().getId());
        } else if (args.get(0).equalsIgnoreCase("start")) {
            if (isRunning) {
                event.getChannel().sendMessage("The ANC is already running!").queue();
                return;
            }
            anc = Executors.newScheduledThreadPool(1);
            isRunning = true;
            anc.scheduleAtFixedRate(thread, 0, delay, TimeUnit.HOURS);
            scheduledFuture = anc.scheduleAtFixedRate(thread, 0, delay, TimeUnit.HOURS);
            event.getChannel().sendMessage("Started the ANC Executor!").queue();
        } else if (args.get(0).equalsIgnoreCase("stop")) {
            if (!isRunning) {
                event.getChannel().sendMessage("The ANC isn't running :|").queue();
                return;
            }
            isRunning = false;
            anc.shutdown();
            event.getChannel().sendMessage("Stopped the ANC Executor!").queue();
        } else if (args.get(0).equalsIgnoreCase("list")) {
            event.getChannel().sendMessage("**Channels in the ANC Hash**:\n" + ancChannels.toString()).queue();
        } else if (args.get(0).equalsIgnoreCase("clear")) {
            if (ancChannels.isEmpty()) {
                event.getChannel().sendMessage("The ANC Hash is already empty!").queue();
            } else {
                ancChannels.clear();
                event.getChannel().sendMessage("Successfully cleared **" + ancChannels.size() + "** ANC Channels").queue();
            }
        } else if (args.get(0).equalsIgnoreCase("delay")) {
            if (args.size() == 1) {
                event.getChannel().sendMessage("Please insert a delay value").queue();
            } else {
                try {
                    delay = Integer.parseInt(args.get(1));
                    event.getChannel().sendMessage("Channels will now be nuked at `" + delay + "` hour intervals!").queue();
                } catch (Exception ex) {
                    event.getChannel().sendMessage("Please insert a valid delay value").queue();
                }
            }

        } else if (args.get(0).equalsIgnoreCase("clean")) {
            int count = 0;
            for (String s : ancChannels) {
                try {
                    event.getJDA().getTextChannelById(s);
                } catch (NullPointerException ex) {
                    count++;
                    ancChannels.remove(s);
                }
            }
            event.getChannel().sendMessage("Cleaned up **" + count + "** ANC Channels!").queue();
        } else if (args.get(0).equalsIgnoreCase("cd") || args.get(0).equalsIgnoreCase("cooldown")) {
            if (isRunning) {
                event.getChannel().sendMessageFormat("`%s` minutes until next channel nuke.", scheduledFuture.getDelay(TimeUnit.MINUTES)).queue();
            } else {
                event.getChannel().sendMessage("The ANC is not running!").queue();
            }
        }

    }

    @Override
    public String getHelp() {
        return "Remakes all channels after the specified amount of time (default: 3 hours)\n" + "`" + Core.PREFIX + getInvoke() + " [start/stop/list/clear/clean/cd/delay/(h)]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\nCurrently: `" + (isRunning ? "enabled" : "disabled") + "` at `" + delay + "` hour intervals";
    }

    @Override
    public String getInvoke() {
        return "autonukechannels";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"autonukechannel", "anc"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
