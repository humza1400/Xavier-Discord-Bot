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

        int amount;

        if ((event.getAuthor().getIdLong() != Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to give credits, sorry bro").queue();
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
                amount = Integer.parseInt(args.get(0));
            else
                amount  = Integer.parseInt(args.get(1));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("That number is either invalid or too large").queue();
            return;
        }
        if (memberList.isEmpty()) {
            if (EconomyManager.verifyUser(event.getMember().getUser().getId())) {
                EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
            }
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + amount);
            event.getChannel().sendMessage(String.format("Added **%s** credits to the balance of %s!", amount, event.getMember().getUser().getAsMention())).queue();
            EcoJSONHandler.saveEconomyConfig();
        } else {
            if (EconomyManager.verifyUser(memberList.get(0).getUser().getId())) {
                EconomyManager.getUsers().put(memberList.get(0).getUser().getId(), 0);
            }
            EconomyManager.setBalance(memberList.get(0).getUser().getId(), EconomyManager.getBalance(memberList.get(0).getUser().getId()) + amount);
            event.getChannel().sendMessage(String.format("Added **%s** credits to the balance of %s!", amount, memberList.get(0).getUser().getAsMention())).queue();
            EcoJSONHandler.saveEconomyConfig();
        }
    }

    @Override
    public String getHelp() {
        return "Adds the specified amount of credits to your current balance\n" + "`" + Core.PREFIX + getInvoke() + " [user] <amount>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "addbal";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"addbalance","ecogive","moneygive","givebal"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
