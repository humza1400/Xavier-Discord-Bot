package me.comu.exeter.commands.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CatCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedImage(getCatUrl()).build()).queue();
    }

    private String getCatUrl() {
        try {
            JsonArray jsonArray = Wrapper.getJsonFromURL("https://api.thecatapi.com/v1/images/search").getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            return jsonObject.get("url").getAsString();
        } catch (IOException ex) {
            return "Something went wrong! (IOException)";
        }    }


    @Override
    public String getHelp() {
        return "Returns a cat\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "cat";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"kitty"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
