package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CheckBalanceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (EconomyManager.verifyMember(event.getMember()))
        {
            EconomyManager.getUsers().put(event.getMember(), 0.0);
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (!memberList.isEmpty())
        {
            Member member = memberList.get(0);
            if (EconomyManager.verifyMember(member)) EconomyManager.getUsers().put(member, 0.0);
            double balance = EconomyManager.getBalance(member);
            event.getChannel().sendMessage(memberList.get(0).getAsMention() + " has a balance of " + String.format("**%s** credits.", balance)).queue();
        } else {

            double balance = EconomyManager.getUsers().get(event.getMember());
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has a balance of " + String.format("**%s** credits.", balance)).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Check your current balance\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "bal";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"balance","credits","money","$"};
    }
}
