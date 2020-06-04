package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClearEconomyCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to clear the economy!").queue();
            return;
        }
        EconomyManager.getUsers().clear();
        event.getChannel().sendMessage("Successfully cleared all values in the economy hash").queue();
        EcoJSONHandler.saveEconomyConfig();
    }

    @Override
    public String getHelp() {
        return "Resets the economy hash for the bot\n`" + Core.PREFIX + getInvoke() + "` [argument]\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "cleareconomy";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"emptyeco", "nulleco", "nullifyeco", "reseteconomy","purgeeco","purgeeconomy","ecoclear","economyclear","cleareco"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
