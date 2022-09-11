package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CoinflipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify an amount to coinflip.").build()).queue();
            return;
        }
        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId())) {
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
        }
        Member member = event.getMember();
        int balance = EconomyManager.getBalance(member.getUser().getId());
        int  wager;
        if (EconomyManager.getBalance(member.getUser().getId()) == 0)
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You have no money to coinflip! Maybe try begging on the streets scrub!").build()).queue();
            return;
        }
        try {
            wager = Integer.parseInt(args.get(0));
            if (wager < 0)
            {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't coinflip negative credits!").build()).queue();
                return;
            } else if (wager == 0)
            {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't coinflip 0 credits!").build()).queue();
                return;
            }
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That number is either invalid or too large.").build()).queue();
            return;
        }
        if (wager > balance)
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(String.format("You don't have sufficient credits. You only have **%d**.", EconomyManager.getBalance(event.getMember().getId()))).build()).queue();
            return;
        } else
        {
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
            {
                EconomyManager.setBalance(member.getUser().getId(), balance + wager);
                event.getChannel().sendMessageEmbeds(Utility.embed("**Congratulations!** You have won your wager of **" + wager + "** credits.").build()).queue();
            } else {
                EconomyManager.setBalance(member.getUser().getId(), balance - wager);
                event.getChannel().sendMessageEmbeds(Utility.embed("**Yikes**, you lost your wager of **" + wager + "** credits better luck next time.").build()).queue();
            }
        }
        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
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
        return new String[] {"gamble","coinflip","cf","bet"};
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
