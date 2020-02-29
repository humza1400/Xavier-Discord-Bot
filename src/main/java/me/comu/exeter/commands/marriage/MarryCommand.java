package me.comu.exeter.commands.marriage;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MarryCommand implements ICommand {

    public static boolean pending = false;
    public static long marriageChannelID;
    public static long getProposedID;
    public static long getProposerID;
    private EventWaiter eventWaiter;

    public MarryCommand(EventWaiter waiter) {
        this.eventWaiter = waiter;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify who you want to marry.").queue();
            return;
        }
        if (pending)
        {
            event.getChannel().sendMessage("Someone already has a pending marriage request to respond to").queue();
            return;
        }
        List<Member> members = event.getMessage().getMentionedMembers();
        if (members.isEmpty()) {
            event.getChannel().sendMessage("Please specify a valid user to marry.").queue();
            return;
        }
        if (members.get(0).getUser().getId().equals(event.getAuthor().getId())) {
            event.getChannel().sendMessage("You cannot marry yourself, no matter how lonely you may be.").queue();
            return;
        }

        if (members.get(0).getUser().isBot()) {
            event.getChannel().sendMessage("You cannot marry a bot, no matter how lonely you may be.").queue();
            return;
        }

        if (Wrapper.marriedUsers.containsKey(event.getAuthor().getId())) {
            event.getChannel().sendMessage("Bro wtf, you're already married! " + event.getJDA().getUserById(Wrapper.marriedUsers.get(event.getMember().getId())).getAsMention() + " you seeing this?!").queue();
            return;
        }
        if (Wrapper.marriedUsers.containsValue(event.getMember().getId())) {
            event.getChannel().sendMessage("Bro wtf, you're already married! " + event.getJDA().getUserById(Wrapper.getKeyByValue(Wrapper.marriedUsers, event.getMember().getId())).getAsMention() + " you seeing this?!").queue();
            return;
        }
        if (Wrapper.marriedUsers.containsKey(members.get(0).getId()) || Wrapper.marriedUsers.containsValue(members.get(0).getId())) {
            event.getChannel().sendMessage("Bro, they're already married, let it go.").queue();
        }

        event.getChannel().sendMessage(event.getMember().getUser().getName() + " has requested to marry you, do you accept? (Yes/No) " + members.get(0).getUser().getAsMention()).queue();
        marriageChannelID = event.getChannel().getIdLong();
        getProposerID = event.getAuthor().getIdLong();
        getProposedID = members.get(0).getIdLong();
        pending = true;
    }


    @Override
    public String getHelp() {
        return "Sends a marriage proposal to the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "marry";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"propose"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
