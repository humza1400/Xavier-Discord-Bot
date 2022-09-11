package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ResetAllBalancesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ((event.getAuthor().getIdLong() != Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to reset ALL balances, sorry bro.").build()).queue();
            return;
        }
        EconomyManager.getUsers().clear();
        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully reset everyone's balances to **0** credits").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
