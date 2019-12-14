package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SetBalanceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        double amount;

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("You don't have permission to set balance").queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        try {
            if (memberList.isEmpty())
            amount = Double.parseDouble(args.get(0));
            else
                amount  = Double.parseDouble(args.get(1));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("That number is either invalid or too large").queue();
            return;
        }
        if (EconomyManager.verifyMember(event.getMember()))
        {
            EconomyManager.getUsers().put(event.getMember(), 0.0);
        }
        if (memberList.isEmpty())
        {
            EconomyManager.setBalance(event.getMember(), amount);
            event.getChannel().sendMessage(String.format("Set your balance to **%s**! %s", amount, event.getMember().getAsMention())).queue();
            return;
        }
        if (!memberList.isEmpty()) {
            if (EconomyManager.verifyMember(memberList.get(0))) EconomyManager.getUsers().put(memberList.get(0), 0.0);
            EconomyManager.setBalance(memberList.get(0), amount);
            event.getChannel().sendMessage(String.format("Set the balance of %s to **%s**!", memberList.get(0).getAsMention(), amount)).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Adds the specified value to your current balance\n" + "`" + Core.PREFIX + getInvoke() + " [amount]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setbal";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setbalance","ecoset","seteco","setcredits","setmoney"};
    }
}
