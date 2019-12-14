package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class PurgeUserCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a user to purge").queue();
            return;
        }
    }

    @Override
    public String getHelp() {
        return "Purges messages from a specific user\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "purgeuser";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"userpurge","pu","puser"};
    }
}
