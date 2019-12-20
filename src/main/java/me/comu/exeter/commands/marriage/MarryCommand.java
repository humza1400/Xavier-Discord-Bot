package me.comu.exeter.commands.marriage;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.Timer;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MarryCommand implements ICommand {

    Timer timer = new Timer();
    boolean pending = false;
    EventWaiter eventWaiter;

    public MarryCommand(EventWaiter waiter) {
        this.eventWaiter = waiter;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify who you want to marry.").queue();
            return;
        }
        List<Member> members = event.getMessage().getMentionedMembers();
        Member member = event.getMember();
        if (members.isEmpty()) {
            event.getChannel().sendMessage("Please specify a valid user.").queue();
            return;
        }
            event.getChannel().sendMessage(event.getMember().getUser().getName() + " has requested to marry you, do you accept? (Yes/No) " + members.get(0).getUser().getAsMention()).queue();
            pending = true;
//                while (pending) {
//                    eventWaiter.waitForEvent(MessageReceivedEvent.class, (e) -> (e.isFromType(ChannelType.TEXT) && e.getMember().getId().equals(members.get(0).getId()) && e.getMessage().getContentRaw().equals("Yes")), e -> {
//                        pending = false;
//                        event.getChannel().sendMessage(members.get(0).getUser().getAsMention() + " has accepted " + member.getAsMention() + "'s marriage proposal. Congratulations!").queue();
//                    });
//
//                        eventWaiter.waitForEvent(MessageReceivedEvent.class, (e) -> (e.isFromType(ChannelType.TEXT) && e.getMember().getId().equals(members.get(0).getId()) && e.getMessage().getContentRaw().equals("No")), e -> {
//                            event.getChannel().sendMessage(members.get(0).getUser().getAsMention() + " just rejected" + member.getAsMention() + "'s marriage proposal. Maybe next time bro.").queue();
//                            pending = false;
//
//                        });
//                }
//        if (timer.hasCompleted(10000)) {
//            event.getChannel().sendMessage(member.getAsMention() + " your marriage proposal to " + members.get(0).getAsMention() + " has expired!").queue();
//            pending = false;
//        }
        }


    @Override
    public String getHelp() {
        return "Sends a marriage proposal to the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "marry";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
