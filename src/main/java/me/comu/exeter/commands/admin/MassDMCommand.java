package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MassDMCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> memberList = event.getGuild().getMembers();
        String message = event.getMessage().getContentRaw();
        Thread thread = new Thread("mass-dm thread");
//        Member memberPerms = event.getMember();
//        Long damon = 464114153616048131L;
        if (!(event.getAuthor().getIdLong() == Core.OWNERID /*|| event.getAuthor().getIdLong() == damon*/)) {
            event.getChannel().sendMessage("You don't have permission to dm people, sorry bro").queue();
            return;
        }

        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a message you want to mass-pm to the server").queue();
            return;
        }
        message = message.substring(7);
        int counter = 0;
        System.out.println("Starting mass dm to " + event.getGuild().getMembers().size() + " members in " + event.getGuild().getName() + " (" +event.getGuild().getId() + ")");
        try {
//            Thread t = new Thread(() -> {
//
//            });
            for (Member member : memberList) {
                if (!member.getUser().isBot()) {
                    Wrapper.sendPrivateMessage(member.getUser(), message);
                    counter++;
                    System.out.println("Messaged " + member.getUser().getAsTag() + " (" + counter + ")");
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException ex) {ex.printStackTrace();}
        event.getChannel().sendMessage("Successfuly messaged " + event.getGuild().getMembers().size() + " users!").queue();

    }

//    class massdmThread implements Runnable{
//        @Override
//        public void run() {
//
//        }
//    }
// idea: try using a thread for mass-dm so bot doesnt stop


    @Override
    public String getHelp() {
        return "Spam messages everyone on the server\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "massdm";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"spamdm","spampm"};
    }
}
