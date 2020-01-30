package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AFKCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

    }

    @Override
    public String getHelp() {
        return "Sets your account AFK\n`" + Core.PREFIX + getInvoke() + " [AFK-Message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "afk";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setafk","goafk","afkstatus"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
