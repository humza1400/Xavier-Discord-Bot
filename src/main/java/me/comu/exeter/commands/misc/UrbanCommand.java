package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UrbanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a word to look up").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String link = stringJoiner.toString().replaceAll(" ", "%20");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://mashape-community-urban-dictionary.p.rapidapi.com/define?term=" + link)
                .get()
                .addHeader("x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "994cd14985mshaa0c788e9757c0dp192967jsn161c66f2a722")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                Logger.getLogger().print(jsonResponse + "\n");
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String definition = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("definition").toString().replaceAll("]", "").replaceAll("\\[", "");
                String permalink = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("permalink").toString();
                String example = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("example").toString().replaceAll("]", "").replaceAll("\\[", "");
                String date = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("written_on").toString();
                date = date.replace(date.substring(date.indexOf("T"), date.length() - 1), "").replace(date.substring(date.indexOf("Z"), date.length() - 1), "").replaceAll("[a-zA-Z]", "").replaceAll("-", "/");
                date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.UK)).format(DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US));
                String author = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("author").toString();
                String thumbsup = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("thumbs_up").toString();
                String thumbsdown = jsonObject.getJSONArray("list").getJSONObject(0).toMap().get("thumbs_down").toString();
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Wrapper.getAmbientColor()).setTitle(stringJoiner.toString(), permalink).setFooter(date).addField("Definition", definition, true).addField("Example", example, true).addField("Author", author, true).addField("Thumbs Up", thumbsup, true).addField("Thumbs Down", thumbsdown, true).build()).queue();
            }
        } catch (IOException ex) {
            event.getChannel().sendMessage("Caught IOException").queue();
            ex.printStackTrace();
        } catch (JSONException ex) {
            event.getChannel().sendMessage("Nothing found for " + MarkdownUtil.monospace(Wrapper.removeMentions(stringJoiner.toString()))).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Searches google for images\n`" + Core.PREFIX + getInvoke() + " [word]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "urban";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"urbandictionary", "gethip"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
