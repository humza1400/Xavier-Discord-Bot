package me.comu.exeter.commands.economy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EcoConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String buffer;
            BufferedReader bufferedReader = new BufferedReader(new FileReader("economy.json"));
            while ((buffer = bufferedReader.readLine()) != null) {
                stringBuilder.append(buffer);
            }
        } catch (IOException ex) {
            event.getChannel().sendMessage("Couldn't locate `economy.json`").queue();
            ex.printStackTrace();
            return;
        }
        String response = stringBuilder.toString();
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement el = parser.parse(response);
        response = gson.toJson(el);
        if (response.length() >= 2000) {
            event.getChannel().sendMessage("File too large!").queue();
            return;
        }
        event.getChannel().sendMessage(MarkdownUtil.codeblock("json", response)).queue();
        EcoJSONHandler.saveEconomyConfig();
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
        return new String[] {"ecocfg","bals"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
