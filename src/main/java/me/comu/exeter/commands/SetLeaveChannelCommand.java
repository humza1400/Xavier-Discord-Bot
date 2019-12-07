package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SetLeaveChannelCommand implements ICommand {

    public static boolean bound = false;
    public static long logChannelID;
    public static String channelName;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("You don't have permission to set the farewell channel").queue();
            return;
        }
        if (bound == true) {
            event.getChannel().sendMessage("Farewell channel already bound. Nullifying...").queue();
        }
        TextChannel channel = event.getChannel();
        this.logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessage("Farewell channel bound to `#" + channelName + "`").queue();
        bound = true;
    }

    @Override
    public String getHelp() {
        return "Binds the current text-channel to send farewell messages\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setfarewell";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setleave","setfarewellmessages","setleavemessages"};
    }
}
