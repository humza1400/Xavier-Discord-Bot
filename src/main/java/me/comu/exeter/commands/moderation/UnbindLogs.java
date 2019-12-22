package me.comu.exeter.commands.moderation;

import me.comu.exeter.commands.moderation.BindLogChannelCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class UnbindLogs implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to unbind a log channel").queue();
            return;
        }
        if (BindLogChannelCommand.bound) {
            String name = BindLogChannelCommand.channelName;
            BindLogChannelCommand.bound = false;
            event.getChannel().sendMessage("Successfully nullified log-channel: " + name).queue();
        } else {
            event.getChannel().sendMessage("There is no log-channel currently set. Try " + Core.PREFIX + "bindlogs").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Unbinds the current log channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\nCurrent log-channel: `" + (BindLogChannelCommand.bound ? BindLogChannelCommand.channelName : "null")+ "`";
    }

    @Override
    public String getInvoke() {
        return "unbind";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"unbindlogs"};
    }
}
