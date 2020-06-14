package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AutoMuteCommand implements ICommand {


    public static int threshold = 3;
    public static boolean active = false;
    public static HashMap<String, Integer> users = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("You don't have permission to toggle Auto-Mute").queue();
            return;
        }
        if (!SetMuteRoleCommand.isMuteRoleSet(event.getGuild())) {
            event.getChannel().sendMessage("Please specify a mute-role before you set-up Auto-Mute").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("threshold") || args.get(0).equalsIgnoreCase("chances") || args.get(0).equalsIgnoreCase("attempts") || args.get(0).equalsIgnoreCase("range")) {
            if (args.size() != 2 && !StringUtils.isNumeric(args.get(1))) {
                event.getChannel().sendMessage("Please specify a valid threshold number").queue();
                return;
            }
            threshold = Integer.parseInt(args.get(1));
            event.getChannel().sendMessage("Auto-Mute threshold successfuly set to `" + threshold + "`").queue();
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                event.getChannel().sendMessage("Auto-Mute is now active").queue();
            } else
                event.getChannel().sendMessage("Auto-Mute is already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessage("Auto-Mute is no longer active").queue();
            } else
                event.getChannel().sendMessage("Auto-Mute is already disabled").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Auto-mutes a user if they mass-mention or spam invites\n`" + Core.PREFIX + getInvoke() + " [on/off]/[threshold]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`", active ? "enabled" : "disabled") + " at a threshold of `" + threshold + "`";
    }

    @Override
    public String getInvoke() {
        return "automute";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"automod"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
