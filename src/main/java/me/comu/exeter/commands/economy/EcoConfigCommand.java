package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class EcoConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(new JSONObject(EconomyManager.getUsers()).toString()).queue();
        EcoJSONLoader.saveEconomyConfig();
    }

    @Override
    public String getHelp() {
        return "Returns a JSON file output of the economy system\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ecoconfig";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
