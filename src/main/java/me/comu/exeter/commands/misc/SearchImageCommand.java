package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SearchImageCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify what image you want to search for").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        event.getChannel().sendMessage(EmbedUtils.embedImage(Wrapper.extractUrls(searchImage(stringJoiner.toString())).get(1)).setTitle(Wrapper.removeMentions(stringJoiner.toString())).setColor(Wrapper.getAmbientColor()).build()).queue(message -> {
            message.addReaction("\u23EE").queue();
            message.addReaction("\u23ED").queue();
        });
    }
    public static String searchImage(String query) {
        String finRes = "";

        try {
            String googleUrl = "https://www.google.com/search?tbm=isch&q=" + query.replace(",", "");
            Document doc1 = Jsoup.connect(googleUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36").timeout(10 * 1000).get();
            Element media = doc1.select("[data-src]").first();
            String finUrl = media.attr("abs:data-src");

            finRes= "<a href=\"http://images.google.com/search?tbm=isch&q=" + query + "\"><img src=\"" + finUrl.replace("&quot", "") + "\" border=1/></a>";

        } catch (Exception e) {
            System.out.println(e);
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
}
