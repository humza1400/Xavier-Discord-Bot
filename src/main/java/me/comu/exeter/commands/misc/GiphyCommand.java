package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class GiphyCommand implements ICommand {

    @SuppressWarnings("all")
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            String url = "https://api.giphy.com/v1/gifs/random?\u0061\u0070\u0069\u005f\u006b\u0065\u0079\u003d\u004a\u0057\u0077\u0052\u0035\u0030\u0079\u0041\u0033\u0033\u006e\u0057\u006b\u0059\u0075\u0036\u0042\u0057\u004e\u0071\u0033\u006b\u0033\u004e\u0065\u004b\u0076\u0042\u0050\u0055\u004e\u0078&tag=&rating=R";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObject = (JSONObject) new JSONObject(jsonResponse).get("data");
                    HashMap<String, HashMap> hashMap = (HashMap<String, HashMap>) jsonObject.toMap().get("images");
                    String gifUrl = (String) hashMap.get("downsized_large").get("url");
                    event.getChannel().sendMessageEmbeds(Utility.embedImage(gifUrl).setColor(Core.getInstance().getColorTheme()).build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Response was unsuccessful. Something went wrong ig").build()).queue();
                    System.out.println(response.message());
                }
            } catch (IOException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong with the endpoint").build()).queue();
                ex.printStackTrace();
            }
        } else {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String url = String.format("https://api.giphy.com/v1/gifs/search?\u0061\u0070\u0069\u005f\u006b\u0065\u0079\u003d\u004a\u0057\u0077\u0052\u0035\u0030\u0079\u0041\u0033\u0033\u006e\u0057\u006b\u0059\u0075\u0036\u0042\u0057\u004e\u0071\u0033\u006b\u0033\u004e\u0065\u004b\u0076\u0042\u0050\u0055\u004e\u0078&q=%s&limit=1&offset=0&rating=R&lang=en", stringJoiner.toString());
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).get().build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONArray dataArray = (JSONArray) jsonObject.get("data");
                    if (dataArray.isEmpty()) {
                        event.getChannel().sendMessageEmbeds(Utility.embed("No GIF found for `" + Utility.removeMentions(stringJoiner.toString())).build()).queue();
                        return;
                    }
                    HashMap<String, HashMap> hashMap = (HashMap<String, HashMap>) dataArray.getJSONObject(0).toMap().get("images");
                    String gifUrl = (String) hashMap.get("downsized_large").get("url");
                    event.getChannel().sendMessageEmbeds(Utility.embedImage(gifUrl).setColor(Core.getInstance().getColorTheme()).setTitle(stringJoiner.toString()).build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Response was unsuccessful. Something went wrong ig").build()).queue();
                    System.out.println(response.message());
                }
            } catch (Exception ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong with the endpoint").build()).queue();
                ex.printStackTrace();
            }
        }
    }


    @Override
    public String getHelp() {
        return "Returns a GIF (leave blank for random)\n`" + Core.PREFIX + getInvoke() + "[search-query]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "giphy";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"gif", "randomgif", "searchgif", "gifsearch", "gifrandom", "searchgiphy", "giphysearch", "randomgif", "tenor"};
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
