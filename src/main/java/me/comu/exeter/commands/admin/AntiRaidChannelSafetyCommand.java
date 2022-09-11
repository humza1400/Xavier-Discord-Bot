package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.RestorableCategory;
import me.comu.exeter.objects.RestorableChannel;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntiRaidChannelSafetyCommand implements ICommand {
    private static boolean active = false;
    public static final List<RestorableCategory> restorableCategories = new ArrayList<>();
    public static final List<RestorableChannel> restorableChannels = new ArrayList<>();
    private ScheduledExecutorService arcs;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle ARCS").build()).queue();

        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle ARCS").build()).queue();
            return;

        }
        StringBuilder categories = new StringBuilder("`Restorable Categories (" + restorableCategories.size() + ")`\n");
        StringBuilder channels = new StringBuilder("`Restorable Channels (" + restorableCategories.size() + ")`\n");
        for (RestorableCategory restorableCategory : restorableCategories) {
            categories.append(restorableCategory.getName()).append("\n");
        }
        for (RestorableChannel restorableChannel : restorableChannels) {
            channels.append(restorableChannel.getName()).append("\n");
        }
        event.getChannel().sendMessage(categories.toString()).queue();
        event.getChannel().sendMessage(channels.toString()).queue();
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(getHelp()).build()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle ARCS.").build()).queue();
            return;
        }
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permission to toggle ARCS.").build()).queue();
            return;
        }

        Runnable backup = (() -> {
            restorableChannels.clear();
            restorableCategories.clear();
            Core.getInstance().getJDA().getCategories().forEach(category -> restorableCategories.add(new RestorableCategory(category)));
            Core.getInstance().getJDA().getTextChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
            Core.getInstance().getJDA().getVoiceChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
        });
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                arcs = Executors.newScheduledThreadPool(1);
                arcs.scheduleAtFixedRate(backup, 0, 3, TimeUnit.HOURS);
                active = true;
                event.getChannel().sendMessageEmbeds(Utility.embed("ARCS is now **active**.").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("ARCS is already enabled.").build()).queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                clear();
                arcs.shutdown();
                active = false;
                event.getChannel().sendMessageEmbeds(Utility.embed("ARCS is **no longer active**.").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("ARCS is already disabled.").build()).queue();
        }
    }

    public static void restore() {
        Core.getInstance().getJDA().getCategories().forEach(category -> restorableCategories.add(new RestorableCategory(category)));
        Core.getInstance().getJDA().getTextChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
        Core.getInstance().getJDA().getVoiceChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
    }

    public static void clear() {
        restorableCategories.clear();
        restorableChannels.clear();
    }

    private <Z> List<Z> addElement(List<Z> list, Z element) {
        list.add(element);
        return list;
    }

    public static boolean isActive() {
        return active;
    }

    @Override
    public String getHelp() {
        return "Recreates a channel if it's deleted (Anti-Raid Must be Enabled)\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "antiraidchannelsafety";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"arcs"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return true;
    }

}
