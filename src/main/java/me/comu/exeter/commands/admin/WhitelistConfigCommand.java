package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class WhitelistConfigCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String hashMap = new JSONObject(WhitelistCommand.getWhitelistedIDs()).toString().replaceAll("\\{", "{\n").replaceAll(",", ",\n").replaceAll("}", "\n}");
        event.getChannel().sendMessage("```json\n" + hashMap + "```").queue();
        WhitelistedJSONHandler.saveWhitelistConfig();
    }


    @Override
    public String getHelp() {
        return "Returns a JSON file output of the whitelist config\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "whitelistconfig";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"whitelistcfg", "wlcfg", "wlconfig", "whitelistedconfig", "wldconfig", "wldcfg"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}


