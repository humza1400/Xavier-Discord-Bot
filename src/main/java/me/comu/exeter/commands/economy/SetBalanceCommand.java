package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetBalanceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        int amount;

        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set balances, sorry bro.").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid user an amount.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("all") || args.get(0).equalsIgnoreCase("everyone") && args.size() == 2) {
            for (String x : EconomyManager.getUsers().keySet()) {
                try {
                    EconomyManager.setBalance(x, Integer.parseInt(args.get(1)));
                } catch (NumberFormatException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That number is either invalid or too large.").build()).queue();
                    return;
                }
            }
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully set everyone's balance to **" + args.get(1) + "** credits.").build()).queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        try {
            if (memberList.isEmpty())
                amount = Integer.parseInt(args.get(0));
            else
                amount = Integer.parseInt(args.get(1));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That number is either invalid or too large.").build()).queue();
            return;
        }
//        if (EconomyManager.verifyUser(event.getMember().getUser()))
//        {
//            EconomyManager.getUsers().put(event.getMember().getUser(), 0.0);
//        }
        if (memberList.isEmpty()) {
            EconomyManager.setBalance(Objects.requireNonNull(event.getMember()).getUser().getId(), amount);
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Set your balance to **%s**! %s", amount, event.getMember().getAsMention())).build()).queue();
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            return;
        }
//            if (EconomyManager.verifyUser(memberList.get(0).getUser())) EconomyManager.getUsers().put(memberList.get(0).getUser(), 0.0);
        EconomyManager.setBalance(memberList.get(0).getUser().getId(), amount);
        event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Set the balance of %s to **%s**!", memberList.get(0).getAsMention(), amount)).build()).queue();

        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
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
        return new String[]{"setbalance", "ecoset", "seteco", "setcredits", "setmoney"};
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
