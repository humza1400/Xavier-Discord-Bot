package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CheckBalanceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId()))
        {
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (!memberList.isEmpty())
        {
            Member member = memberList.get(0);
            if (EconomyManager.verifyUser(member.getUser().getId())) EconomyManager.getUsers().put(member.getUser().getId(), 0);
            double balance = EconomyManager.getBalance(member.getUser().getId());
            event.getChannel().sendMessageEmbeds(Utility.embed(memberList.get(0).getUser().getAsMention() + " has a balance of " + String.format("**%s** credits.", balance)).build()).queue();
        } else {

            double balance = EconomyManager.getBalance((event.getMember().getUser().getId()));
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getUser().getAsMention() + " has a balance of " + String.format("**%s** credits.", balance)).build()).queue();
        }
        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
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

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
