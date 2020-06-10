package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.EcoJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class EcoSaveConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EcoJSONHandler.saveEconomyConfig();
        event.getChannel().sendMessage("Successfully saved config: `economy.json`").queue();

    }

    @Override
    public String getHelp() {
        return "Force saves the current economy config\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ecosave";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"saveecoconfig","ecosaveconfig"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
