package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SearchImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify what image you want to search for").build()).queue();
            return;
        }

        String query = String.join(" ", args);
        List<String> images = searchImage(query);
        if (images == null || images.isEmpty()) {
            String scrapedImage = scrapeImage(query);
            if (scrapedImage.equalsIgnoreCase("null")) {
                event.getChannel().sendMessageEmbeds(Utility.embed("No results found for **" + Utility.removeMarkdown(Utility.removeMentions(query)) + "**").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.embedImage(Utility.extractUrls(scrapeImage(query)).get(0)).setTitle(query).setColor(Core.getInstance().getColorTheme()).build()).queue();
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            ArrayList<Page> pages = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                eb.clear();
                eb.setTitle(query);
                eb.setImage(images.get(i));
                eb.setFooter(String.format("Page %s/10", i + 1));
                eb.setColor(Core.getInstance().getColorTheme());
                pages.add(new Page(PageType.EMBED, eb.build()));
            }
            event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
        }
    }

    @SuppressWarnings("all")
    private List<String> searchImage(String query) {
        List<String> images = new ArrayList<>();
        String ckey = "\u0041\u0049\u007a\u0061\u0053\u0079\u0041\u006c\u0073\u0039\u007a\u0072\u0056\u0056\u0051\u0074\u005a\u006b\u0073\u006d\u002d\u0074\u004d\u0072\u004b\u004c\u0068\u006d\u0058\u0078\u0033\u0054\u0031\u0068\u0072\u0074\u005f\u0035\u0063";
        String cx = "017550937188421690135:io5p5qt70e8";
        String endpoint = String.format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&fileType=jpg,png,jpeg,gif&q=%s&searchType=image&num=10&start=1", ckey, cx, query.replaceAll(" ", "%20"));
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(endpoint).get().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36").addHeader("Content-Type", "application/json").build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                List<HashMap<String, String>> items = (ArrayList) jsonObject.toMap().get("items");
                for (HashMap<String, String> hashMap : items) {
                    images.add(hashMap.get("link"));
                }
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException ex) {
            return null;
        }
        return images;
    }

    private String scrapeImage(String query) {
        String finRes;

        try {
            String googleUrl = "https://www.google.com/search?tbm=isch&q=" + query.replace(",", "");
            Document doc1 = Jsoup.connect(googleUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36").timeout(10 * 1000).get();
            Element media = doc1.select("[data-src]").first();
            String finUrl = media.attr("abs:data-src");
            finRes = "<a href=\"http://images.google.com/search?tbm=isch&q=" + query + "\"><img src=\"" + finUrl.replace("&quot", "") + "\" border=1/></a>";

        } catch (Exception e) {
            return "null";
        }

        return finRes;
    }

    @Override
    public String getHelp() {
        return "Searches google for images\n`" + Core.PREFIX + getInvoke() + " [image-search]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "imagesearch";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"image", "img", "searchimage"};
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
