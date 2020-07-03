package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.RestorableCategory;
import me.comu.exeter.objects.RestorableChannel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntiRaidChannelSafetyCommand implements ICommand {
    private static boolean active = false;
    public static List<RestorableCategory> restorableCategories = new ArrayList<>();
    public static List<RestorableChannel> restorableChannels = new ArrayList<>();
    private ScheduledExecutorService arcs;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        StringBuilder categories = new StringBuilder("`Restorable Categories (" + restorableCategories.size() + ")`\n");
        StringBuilder channels = new StringBuilder("`Restorable Channels (" + restorableCategories.size() + ")`\n");
        for (RestorableCategory restorableCategory : restorableCategories)
        {
            categories.append(restorableCategory.getName()).append("\n");
        }
        for (RestorableChannel restorableChannel : restorableChannels)
        {
            channels.append(restorableChannel.getName()).append("\n");
        }
        event.getChannel().sendMessage(categories.toString()).queue();
        event.getChannel().sendMessage(channels.toString()).queue();
        if (args.isEmpty()) {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to toggle ARCS").queue();
            return;
        }
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.getChannel().sendMessage("I don't have permission to toggle ARCS").queue();
            return;
        }
        Thread backup = new Thread(() -> {
            restorableChannels.clear();
            restorableCategories.clear();
            Core.jda.getCategories().forEach(category -> restorableCategories.add(new RestorableCategory(category)));
            Core.jda.getTextChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
            Core.jda.getVoiceChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
        });
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                arcs = Executors.newScheduledThreadPool(1);
                arcs.scheduleAtFixedRate(backup, 0, 3, TimeUnit.HOURS);
                active = true;
                event.getChannel().sendMessage("ARCS is now active").queue();
            } else
                event.getChannel().sendMessage("ARCS is already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                clear();
                arcs.shutdown();
                active = false;

                event.getChannel().sendMessage("ARCS is no longer active").queue();
            } else
                event.getChannel().sendMessage("ARCS is already disabled").queue();
        }
    }

    public static void restore()
    {
        Core.jda.getCategories().forEach(category -> restorableCategories.add(new RestorableCategory(category)));
        Core.jda.getTextChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
        Core.jda.getVoiceChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
    }

    public static void clear()
    {
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


}
