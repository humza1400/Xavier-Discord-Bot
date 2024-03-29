package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnbindLogs implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to unbind a log channel").build()).queue();
            return;
        }
        if (BindLogChannelCommand.bound) {
            String name = BindLogChannelCommand.channelName;
            BindLogChannelCommand.bound = false;
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully nullified log-channel: " + name).build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There is no log-channel currently set. Try " + Core.PREFIX + "bindlogs").build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Unbinds the current log channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\nCurrent log-channel: `" + (BindLogChannelCommand.bound ? BindLogChannelCommand.channelName : "null") + "`";
    }

    @Override
    public String getInvoke() {
        return "unbind";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"unbindlogs"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
