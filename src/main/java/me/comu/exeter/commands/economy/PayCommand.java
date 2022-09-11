package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
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
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a member you want to send the money to").build()).queue();
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
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't pay negative credits!").build()).queue();
                return;
            } else if (amount == 0)
            {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't pay 0 credits!").build()).queue();
                return;
            }
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That number is either invalid or too large").build()).queue();
            return;
        }
        if (EconomyManager.getBalance(event.getMember().getUser().getId()) < amount)
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have **" + amount + "** credits to send!").build()).queue();
            return;
        }
        if (!memberList.isEmpty()) {
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) - amount);
            EconomyManager.setBalance(memberList.get(0).getUser().getId(), EconomyManager.getBalance(memberList.get(0).getUser().getId()) + amount);
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("%s has transferred **%s** credits to %s!", event.getMember(), amount, memberList.get(0).getAsMention())).build()).queue();

        }
        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
