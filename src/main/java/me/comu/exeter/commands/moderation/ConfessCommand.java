package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ConfessCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
    if (!SetConfessionChannelCommand.bound)
    {
        event.getChannel().sendMessage("There is currently no confession channel bound, contact your server administrator.").queue();
    } else {
        event.getChannel().sendMessage("To submit your confession please DM the bot `" + Core.PREFIX + "confess [confession]`\n*Remember to keep things apropriate and to follow Discord TOS.*").queue();
    }


    }

    @Override
    public String getHelp() {
        return "Creates a confession\n `" + Core.PREFIX + getInvoke() + " [confession]`\nAliases:`" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "confess";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
