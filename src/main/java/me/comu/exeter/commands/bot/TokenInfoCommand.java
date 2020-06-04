package me.comu.exeter.commands.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TokenInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a token").queue();
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://canary.discordapp.com/api/v7/users/@me").get()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", args.get(0))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JsonParser parser = new JsonParser();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement el = parser.parse(jsonResponse);
                jsonResponse = gson.toJson(el);
                event.getChannel().sendMessage(MarkdownUtil.codeblock(jsonResponse)).queue();
            } else {
                event.getChannel().sendMessage("Token Couldn't Be Resolved | " + response.code() + " " + response.message()).queue();
            }

        } catch (IOException ex) {
            event.getChannel().sendMessage("Connection Throttled When Making Request").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Shows information about the specified token\n`" + Core.PREFIX + getInvoke() + " ` [token]\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "tokeninfo";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
