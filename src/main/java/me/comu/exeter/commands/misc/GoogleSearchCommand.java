package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GoogleSearchCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please enter a search query").build()).queue();
            return;
        }
        List<HashMap<String, String>> results = search(String.join(" ", args));
        String query = String.join(" ", args);
        if (results == null || results.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("No search results found for `" + Utility.removeMentionsAndMarkdown(query) + "`").build()).queue();
        } else {
            if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                EmbedBuilder eb = new EmbedBuilder();
                ArrayList<Page> pages = new ArrayList<>();
                for (int i = 0; i < results.size(); i++) {
                    eb.clear();
                    eb.setTitle(Utility.removeMarkdown(results.get(i).get("title")), results.get(i).get("link"));
                    eb.setDescription(Utility.removeMarkdown(results.get(i).get("snippet")));
                    eb.setFooter(String.format("Search Result %s/10", i+1) + " \u2022 " + results.get(0).get("searches") + " results \u2022 " + results.get(0).get("searchTime") + " seconds");
                    eb.setColor(Core.getInstance().getColorTheme());
                    pages.add(new Page(PageType.EMBED, eb.build()));
                }
                event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
            } else {
                event.getChannel().sendMessage(results.get(0).get("link") + " - *" + Utility.removeMarkdown(results.get(0).get("snippet")) + "*").queue();
            }
        }
    }


    @SuppressWarnings("all")
    private List<HashMap<String, String>> search(String query) {
        List<HashMap<String, String>> results = new ArrayList<>();
        String ckey = "\u0041\u0049\u007a\u0061\u0053\u0079\u0041\u006c\u0073\u0039\u007a\u0072\u0056\u0056\u0051\u0074\u005a\u006b\u0073\u006d\u002d\u0074\u004d\u0072\u004b\u004c\u0068\u006d\u0058\u0078\u0033\u0054\u0031\u0068\u0072\u0074\u005f\u0035\u0063";
        String cx = "017550937188421690135:io5p5qt70e8";
        String endpoint = String.format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s&num=10&start=1", ckey, cx, query.replaceAll(" ", "%20"));
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(endpoint).get().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36").addHeader("Content-Type", "application/json").build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                HashMap<String, String> searchInfo = (HashMap<String, String>) jsonObject.toMap().get("searchInformation");
                results = (ArrayList) jsonObject.toMap().get("items");
                results.get(0).put("searchTime", searchInfo.get("formattedSearchTime"));
                results.get(0).put("searches", searchInfo.get("formattedTotalResults"));
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException ex) {
            return null;
        }
        return results;
    }

    @Override
    public String getHelp() {
        return "Searches google for a search result\n`" + Core.PREFIX + getInvoke() + " [query]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "google";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"search", "gsearch", "googlesearch", "searchgoogle"};
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
