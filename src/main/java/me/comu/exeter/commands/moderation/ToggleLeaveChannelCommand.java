package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ToggleLeaveChannelCommand implements ICommand {

    private static boolean active = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member memberPerms = event.getMember();
        if (args.isEmpty())
        {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (!memberPerms.hasPermission(Permission.MANAGE_SERVER) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to toggle farewell messages").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on"))
        {
            if (!active) {
                active = true;
                event.getChannel().sendMessage("Leave messages will now be sent when a user leaves").queue();
            }
            else
                event.getChannel().sendMessage("Farewell messages are already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off"))
        {
            if (active) {
                active = false;
                event.getChannel().sendMessage("Leave messages will no longer be sent").queue();
            } else
                event.getChannel().sendMessage("Farewell messages are already disabled").queue();
        }
    }


    public static boolean isActive()
    {
        return active;
    }

    @Override
    public String getHelp() {
        return "Sends a message when a user leaves the server\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "leavemessages";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"toggleleave","leavemessage","farewellmessage","farewellmessages","leavemsgs","farewellmsgs"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
