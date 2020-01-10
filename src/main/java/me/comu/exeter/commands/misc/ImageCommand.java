package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ImageCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

    }

    @Override
    public String getHelp() {
        return "Returns a google image result of the specified keywords\n`" + Core.PREFIX + getInvoke() + " [keyword]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "img";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"googleimages","googleimg","img"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
