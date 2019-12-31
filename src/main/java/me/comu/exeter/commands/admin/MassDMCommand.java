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
        String message = event.getMessage().getContentRaw().substring(7);
        if ((event.getAuthor().getIdLong() != Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to mass-dm people, sorry bro").queue();
            return;
        }

        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a message you want to mass-pm to the server").queue();
            return;
        }
        System.out.println("Starting mass dm to " + event.getGuild().getMembers().size() + " members in " + event.getGuild().getName() + " (" +event.getGuild().getId() + ")");
//        try {  /*for (Member member : memberList) {
//                if (!member.getUser().isBot()) {
//                    Wrapper.sendPrivateMessage(member.getUser(), message);
//                    counter++;
//                    System.out.println("Messaged " + member.getUser().getAsTag() + " (" + counter + ")");
//                    Thread.sleep(2000);
//                }
//            }
//        } catch (InterruptedException ex) {ex.printStackTrace();}*/
            Thread massDM = new Thread(() -> {
                try {
                    int counter = 0;
                    for (Member member : memberList) {
                        if (!member.getUser().isBot()) {
                            String finalMessage = message;
                            Wrapper.sendPrivateMessage(member.getUser(), finalMessage);
                            counter++;
                            System.out.println("Messaged " + member.getUser().getAsTag() + " (" + counter + ")");
                            Thread.sleep(2000);
                        }
                    }
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            });
            massDM.start();
        event.getChannel().sendMessage("Messaging " + event.getGuild().getMemberCount()  + " users!").queue();
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
        return "Mass DMs the specified messages to everyone on the server\n`" + Core.PREFIX + getInvoke() + " [message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "massdm";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"spamdm","spampm"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
