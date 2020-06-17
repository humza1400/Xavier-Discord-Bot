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
        if (args.isEmpty()) {
            if (!Wrapper.isMarried(event.getAuthor().getId())) {
                event.getChannel().sendMessage("You're not married to anyone.").queue();
            } else {
                try {
                    event.getJDA().retrieveUserById(Wrapper.getMarriedUser(event.getAuthor().getId())).queue(user ->
                            event.getChannel().sendMessage("You are happily married to " + user.getAsMention()).queue());
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessage("Uh oh, looks like the user you were married to has left you.").queue();
                    Wrapper.marriedUsers.remove(event.getAuthor().getId());
                }
            }
            return;
        }

        if (members.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user").queue();
            return;
        }
        if (!Wrapper.isMarried(members.get(0).getId())) {
            event.getChannel().sendMessage("That user isn't married to anyone").queue();
        } else {
            try {
                event.getJDA().retrieveUserById(Wrapper.getMarriedUser(members.get(0).getId())).queue(user -> event.getChannel().sendMessage(members.get(0).getAsMention() + " is happily married to " + user.getAsMention()).queue());
            } catch (NullPointerException ex) {
                event.getChannel().sendMessage("Uh oh, looks like the user " + members.get(0).getAsMention() + " was married to has left them.").queue();
                Wrapper.marriedUsers.remove(members.get(0).getId());
            }
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
        return new String[]{"spouse"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
