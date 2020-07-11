package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.EcoJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RobCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId())) {
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
        }
        if (memberList.isEmpty()) {
            event.getChannel().sendMessage("Please specify someone to rob from.").queue();
            return;
        }
        if (EconomyManager.verifyUser(memberList.get(0).getUser().getId()))
            EconomyManager.getUsers().put(memberList.get(0).getUser().getId(), 0);
        if (EconomyManager.getBalance(event.getMember().getId()) < 250)
        {
            event.getChannel().sendMessage("You need at least **250** credits to try and rob someone.").queue();
            return;
        }
        Member member = memberList.get(0);
        double d = Math.random();
                if (d >= 0.75)
                {
                    int robbedBalance = Utility.randomNum(0, (EconomyManager.getBalance(member.getUser().getId()) / 8));
                    EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + robbedBalance);
                    EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) - robbedBalance);
                    event.getChannel().sendMessage(String.format("%s just caught %s lacking and finnesed him for **%s** credits LMAO.", event.getMember().getAsMention(), member.getAsMention(), robbedBalance)).queue();
                } else if(d <= 0.45) {
                    EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - EconomyManager.getBalance(event.getMember().getId()) / 8);
                    event.getChannel().sendMessage(String.format("%s failed to rob %s and lost **%s** credits, yikes.", event.getMember().getAsMention(), member.getAsMention(), EconomyManager.getBalance(event.getMember().getId()) / 8)).queue();
                } else {
            int robbedBalance = Utility.randomNum(0, (EconomyManager.getBalance(event.getMember().getUser().getId()) / 8));
            EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) + robbedBalance);
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) - robbedBalance);
            event.getChannel().sendMessage(String.format("%s stay with the strap and instead robbed %s for **%s** credits LOL.", member.getAsMention(), event.getMember().getAsMention(), robbedBalance)).queue();
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
        return new String[]{"finnese", "finesse"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
