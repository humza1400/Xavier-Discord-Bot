package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AntiRaidChannelSafetyCommand implements ICommand {

    private static boolean active = false;
    public static HashMap<String, Integer> channelPositions = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to toggle ARCS").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                event.getChannel().sendMessage("ARCS is now active").queue();
            } else
                event.getChannel().sendMessage("ARCS is already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessage("ARCS is no longer active").queue();
            } else
                event.getChannel().sendMessage("ARCS is already disabled").queue();
        }

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
