package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ForcePendingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a value to set the pending value to").queue();
            return;
        }
        if (!MarryCommand.pending)
        {
            event.getChannel().sendMessage("There is no current value pending").queue();
            return;
        }
        MarryCommand.pending = false;
        event.getChannel().sendMessage("Successfully set pending marriage value to false :thumbsup:").queue();
    }

    @Override
    public String getHelp() {
        return "Force sets a pending marriage value to false.\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "forcepending";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setpending","pending"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
