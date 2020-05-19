package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetBalanceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        int amount;

        if ((event.getAuthor().getIdLong() != Core.OWNERID) && (event.getAuthor().getIdLong() != 699562509366984784L)) {
            event.getChannel().sendMessage("You don't have permission to set balances, sorry bro").queue();
            return;
        }

        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a valid user an amount").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("all") || args.get(0).equalsIgnoreCase("everyone") && args.size() == 2)
        {
            for (String x : EconomyManager.getUsers().keySet())
            {
                try {
                    EconomyManager.setBalance(x, Integer.parseInt(args.get(1)));
                } catch (NumberFormatException ex)
                {
                    event.getChannel().sendMessage("That number is either invalid or too large").queue();
                    return;
                }
            }
            event.getChannel().sendMessage("Successfully set everyone's balance to **" + args.get(1) + "** credits.").queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        try {
            if (memberList.isEmpty())
            amount = Integer.parseInt(args.get(0));
            else
                amount  = Integer.parseInt(args.get(1));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("That number is either invalid or too large").queue();
            return;
        }
//        if (EconomyManager.verifyUser(event.getMember().getUser()))
//        {
//            EconomyManager.getUsers().put(event.getMember().getUser(), 0.0);
//        }
        if (memberList.isEmpty())
        {
            EconomyManager.setBalance(Objects.requireNonNull(event.getMember()).getUser().getId(), amount);
            event.getChannel().sendMessage(String.format("Set your balance to **%s**! %s", amount, event.getMember().getAsMention())).queue();
            EcoJSONHandler.saveEconomyConfig();
            return;
        }
        if (!memberList.isEmpty()) {
//            if (EconomyManager.verifyUser(memberList.get(0).getUser())) EconomyManager.getUsers().put(memberList.get(0).getUser(), 0.0);
            EconomyManager.setBalance(memberList.get(0).getUser().getId(), amount);
            event.getChannel().sendMessage(String.format("Set the balance of %s to **%s**!", memberList.get(0).getAsMention(), amount)).queue();
        }
        EcoJSONHandler.saveEconomyConfig();
    }

    @Override
    public String getHelp() {
        return "Sets the specified value to the specified user's balance\n" + "`" + Core.PREFIX + getInvoke() + " [user] <balance>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setbal";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setbalance","ecoset","seteco","setcredits","setmoney"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
