package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MarriedCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> members = event.getMessage().getMentionedMembers();
        if (args.isEmpty())
        {

            return;
        }

        if (members.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user").queue();
            return;
        }
        if (!Wrapper.isMarried(members.get(0).getId())) {
            event.getChannel().sendMessage("That user isn't married to anyone").queue();
            return;
        }

    }

    @Override
    public String getHelp() {
        return "Checks who the user is married to.\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "married";
    }

    @Override
    public String[] getAlias() {
        return new String[] {};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
