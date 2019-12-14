package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CoinflipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify an amount to coinflip").queue();
            return;
        }
        if (EconomyManager.verifyMember(event.getMember())) {
            EconomyManager.getUsers().put(event.getMember(), 0.0);
        }
        Member member = event.getMember();
        double balance = EconomyManager.getBalance(member);
        double wager;
        if (EconomyManager.getBalance(member) == 0)
        {
            event.getChannel().sendMessage("You have no money to coinflip! Maybe try begging on the streets scrub!").queue();
            return;
        }
        try {
            wager = Double.parseDouble(args.get(0));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("That number is either invalid or too large").queue();
            return;
        }
        if (wager > balance)
        {
            event.getChannel().sendMessage(String.format("You don't have sufficient credits. You only have **%s**.", balance)).queue();
            return;
        }
        if (wager <= balance)
        {
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
            {
                EconomyManager.setBalance(member, balance + wager);
                event.getChannel().sendMessage("**Congratulations!** You have won your wager of **" + wager + "** credits.").queue();
            } else {
                EconomyManager.setBalance(member, balance - wager);
                event.getChannel().sendMessage("Yikes, you lost your wager of " + wager + " credits better luck next time.").queue();
            }
        }

    }

    @Override
    public String getHelp() {
        return "Creates a coinflip with a 50% chance of winning\n" + "`" + Core.PREFIX + getInvoke() + " [amount]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "wager";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"gamble","coinflip","cf"};
    }
}
