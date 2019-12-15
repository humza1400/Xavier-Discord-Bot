package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RobCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (EconomyManager.verifyMember(event.getMember()))
        {
            EconomyManager.getUsers().put(event.getMember(), 0.0);
        }
        if (memberList.isEmpty())
        {
            event.getChannel().sendMessage("Please specify someone to rob from.").queue();
            return;
        }
        if (!memberList.isEmpty()) {
            if (EconomyManager.verifyMember(memberList.get(0))) EconomyManager.getUsers().put(memberList.get(0), 0.0);
            Member member = memberList.get(0);
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
            {
                int robbedBalance = (int) (EconomyManager.getBalance(member)/4);
                EconomyManager.setBalance(event.getMember(), EconomyManager.getBalance(event.getMember()) + robbedBalance);
                EconomyManager.setBalance(member, EconomyManager.getBalance(member) - robbedBalance);
                event.getChannel().sendMessage(String.format("%s just caught %s lacking and finnesed him for **%s** credits LMAO.", event.getMember().getAsMention(), member.getAsMention(), robbedBalance)).queue();
            } else {
                int robbedBalance = (int) EconomyManager.getBalance(event.getMember())/4;
                EconomyManager.setBalance(member, EconomyManager.getBalance(member) + robbedBalance);
                EconomyManager.setBalance(event.getMember(), EconomyManager.getBalance(event.getMember()) - robbedBalance);
                event.getChannel().sendMessage(String.format("%s stay with the strap and instead robbed %s for **%s** credits LOL.", member.getAsMention(), event.getMember().getAsMention(), robbedBalance)).queue();
            }
        }
        EcoJSONLoader.saveEconomyConfig();

    }

    @Override
    public String getHelp() {
        return "Robs another user, need to have purchased a weapon\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rob";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"finnese"};
    }
}
