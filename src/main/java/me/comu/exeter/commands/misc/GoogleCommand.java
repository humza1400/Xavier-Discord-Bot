package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class GoogleCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

    }

    @Override
    public String getHelp() {
        return "Returns google search results for the specified query\n`" + Core.PREFIX + getInvoke() + " [query]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "google";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"googlesearch"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
