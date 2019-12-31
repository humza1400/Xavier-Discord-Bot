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
        if (EconomyManager.verifyUser(event.getMember().getUser().getId()))
        {
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
        }
        if (memberList.isEmpty())
        {
            event.getChannel().sendMessage("Please specify someone to rob from.").queue();
            return;
        }
        if (!memberList.isEmpty()) {
            if (EconomyManager.verifyUser(memberList.get(0).getUser().getId())) EconomyManager.getUsers().put(memberList.get(0).getUser().getId(), 0);
            Member member = memberList.get(0);
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
            {
                int robbedBalance = (int) (EconomyManager.getBalance(member.getUser().getId())/4);
                EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + robbedBalance);
                EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) - robbedBalance);
                event.getChannel().sendMessage(String.format("%s just caught %s lacking and finnesed him for **%s** credits LMAO.", event.getMember().getAsMention(), member.getAsMention(), robbedBalance)).queue();
            } else {
                int robbedBalance = (int) EconomyManager.getBalance(event.getMember().getUser().getId())/4;
                EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) + robbedBalance);
                EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) - robbedBalance);
                event.getChannel().sendMessage(String.format("%s stay with the strap and instead robbed %s for **%s** credits LOL.", member.getAsMention(), event.getMember().getAsMention(), robbedBalance)).queue();
            }
        }
        EcoJSONHandler.saveEconomyConfig();

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
        return new String[] {"finnese","finesse"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
