package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RandomWordCommand implements ICommand {
    @SuppressWarnings("unchecked")
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.thisworddoesnotexist.com/api/random_word.json")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                HashMap<String, String> jsonMap = (HashMap<String, String>) jsonObject.toMap().get("word");
                String word = jsonMap.get("word");
                String definition = jsonMap.get("definition");
                String pos = jsonMap.get("pos");
                String example = jsonMap.get("example");
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Core.getInstance().getColorTheme()).addField("Word", word, false).
                        addField("Definition", definition, false).addField("Part-Of-Speech", pos, false).addField("Example", example, false).setTimestamp(Instant.now()).build()).queue();
            }
        } catch (Exception ex) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Caught an error when connecting to the endpoint").build()).queue();
            ex.printStackTrace();
        }

    }

    @Override
    public String getHelp() {
        return "Scrapes HTTP, HTTPS, SOCKS4, SOCKS5 proxies\n`" + Core.PREFIX + getInvoke() + " [type]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "randomword";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
