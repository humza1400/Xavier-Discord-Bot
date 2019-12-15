package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AddBalanceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        double amount;

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("You don't have permission to give yourself credits").queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify an amount you would like to add to your balance").queue();
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
        if (memberList.isEmpty()) {
            if (EconomyManager.verifyMember(event.getMember())) {
                EconomyManager.getUsers().put(event.getMember(), 0.0);
            }
            EconomyManager.setBalance(event.getMember(), EconomyManager.getBalance(event.getMember()) + amount);
            event.getChannel().sendMessage(String.format("Added **%s** credits to the balance of %s!", amount, event.getMember().getAsMention())).queue();
        } else {
            if (EconomyManager.verifyMember(memberList.get(0))) {
                EconomyManager.getUsers().put(memberList.get(0), 0.0);
            }
            EconomyManager.setBalance(memberList.get(0), EconomyManager.getBalance(memberList.get(0)) + amount);
            event.getChannel().sendMessage(String.format("Added **%s** credits to the balance of %s!", amount, memberList.get(0).getAsMention())).queue();
        }
        EcoJSONLoader.saveEconomyConfig();
    }

    @Override
    public String getHelp() {
        return "Adds the specified amount of credits to your current balance\n" + "`" + Core.PREFIX + getInvoke() + " [amount]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "addbal";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"addbalance","ecogive","moneygive"};
    }
}
