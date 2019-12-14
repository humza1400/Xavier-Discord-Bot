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
        for (Member member : memberList)
        {
            if (!member.getUser().isBot())
                    Wrapper.sendPrivateMessage(member.getUser(), message);


        }
        event.getChannel().sendMessage("Successfuly messaged " + event.getGuild().getMembers().size() + " users!").queue();

    }



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
