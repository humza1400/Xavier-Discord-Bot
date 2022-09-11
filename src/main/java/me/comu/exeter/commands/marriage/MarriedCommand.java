package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MarriedCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> members = event.getMessage().getMentionedMembers();
        if (args.isEmpty()) {
            if (!Utility.isMarried(event.getAuthor().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You're not married to anyone.").build()).queue();
            } else {
                try {
                    event.getJDA().retrieveUserById(Utility.getMarriedUser(event.getAuthor().getId())).queue(user ->
                            event.getChannel().sendMessageEmbeds(Utility.embed("You are happily married to " + user.getAsMention()).build()).queue());
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("Uh oh, looks like the user you were married to has left you.").build()).queue();
                    Utility.marriedUsers.remove(event.getAuthor().getId());
                }
            }
            return;
        }

        if (members.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user.").build()).queue();
            return;
        }
        if (!Utility.isMarried(members.get(0).getId())) {
            event.getChannel().sendMessageEmbeds(Utility.embed("That user isn't married to anyone.").build()).queue();
        } else {
            try {
                event.getJDA().retrieveUserById(Utility.getMarriedUser(members.get(0).getId())).queue(user -> event.getChannel().sendMessageEmbeds(Utility.embed(members.get(0).getAsMention() + " is happily married to " + user.getAsMention()).build()).queue());
            } catch (NullPointerException ex) {
                Utility.marriedUsers.remove(members.get(0).getId());
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
