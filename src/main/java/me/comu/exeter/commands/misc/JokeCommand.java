package me.comu.exeter.commands.misc;

import com.google.gson.JsonElement;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JokeCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
            event.getChannel().sendMessage(randomJoke()).queue();
    }

    private String randomJoke() {
        try {
            JsonElement jsonElement = Wrapper.getJsonFromURL("https://icanhazdadjoke.com/");
            return jsonElement.getAsJsonObject().get("joke").getAsString();
        } catch (IOException ex)
        {
            return "Something went wrong! (IOException)";
        }
    }

    @Override
    public String getHelp() {
        return "Returns a dad joke lmao\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "joke";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
