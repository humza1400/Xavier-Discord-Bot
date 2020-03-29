package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PayCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        int amount;

        List<Member> memberList = event.getMessage().getMentionedMembers();

        if (memberList.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a member you want to send the money to").queue();
            return;
        }

        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId()))

            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);

        if (EconomyManager.verifyUser(memberList.get(0).getUser().getId()))

            EconomyManager.getUsers().put(memberList.get(0).getUser().getId(), 0);

        try {
            if (memberList.isEmpty())
                amount = Integer.parseInt(args.get(0));
            else
                amount  = Integer.parseInt(args.get(1));
            if (amount < 0)
            {
                event.getChannel().sendMessage("You can't pay negative credits!").queue();
                return;
            } else if (amount == 0)
            {
                event.getChannel().sendMessage("You can't pay 0 credits!").queue();
                return;
            }
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("That number is either invalid or too large").queue();
            return;
        }
        if (EconomyManager.getBalance(event.getMember().getUser().getId()) < amount)
        {
            event.getChannel().sendMessage("You don't have **" + amount + "** credits to send!").queue();
            return;
        }
        if (!memberList.isEmpty()) {
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) - amount);
            EconomyManager.setBalance(memberList.get(0).getUser().getId(), EconomyManager.getBalance(memberList.get(0).getUser().getId()) + amount);
            event.getChannel().sendMessage(String.format("%s has transferred **%s** credits to %s!", event.getMember(), amount, memberList.get(0).getAsMention())).queue();
        }
        EcoJSONHandler.saveEconomyConfig();
    }

    @Override
    public String getHelp() {
        return "Pays the specified user the designated amount\n" + "`" + Core.PREFIX + getInvoke() + " [user] <amount>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "pay";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"send","transfer"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
