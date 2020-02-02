package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ResetAllBalancesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ((event.getAuthor().getIdLong() != Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to reset ALL balances, sorry bro").queue();
            return;
        }
        EconomyManager.getUsers().clear();
        EcoJSONHandler.saveEconomyConfig();
        event.getChannel().sendMessage("Successfully reset everyone's balances to **0** credits").queue();
    }

    @Override
    public String getHelp() {
        return "Resets everyone's balance to zero\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "resetbalances";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"resetbals","resetallbals","clearbals","clearallbals"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
