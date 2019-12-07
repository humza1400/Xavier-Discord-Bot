package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class LockdownCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
    TextChannel textChannel = event.getGuild().getTextChannelById("");
    textChannel.getSlowmode();

    }

    @Override
    public String getHelp() {
        return "Locks the current text channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lockdown";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"ld"};
    }
}
