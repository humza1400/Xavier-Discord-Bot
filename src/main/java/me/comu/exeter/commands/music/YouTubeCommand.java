package me.comu.exeter.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class YouTubeCommand implements ICommand {

    private final YouTube youtube;

    public YouTubeCommand() {
        YouTube temp = null;
        try {
            temp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null).setApplicationName("Exeter Discord Bot").build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        youtube = temp;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a search term").build()).queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        event.getChannel().sendMessage(doSearch(stringJoiner.toString())).queue();
    }

    private String doSearch(String queryTerm) {
        String responseUrl = "";
        try {

            YouTube.Search.List search = youtube.search().list("id,snippet");

            String apiKey = "\u0041\u0049\u007a\u0061\u0053\u0079\u0041\u006c\u0073\u0039\u007a\u0072\u0056\u0056\u0051\u0074\u005a\u006b\u0073\u006d\u002d\u0074\u004d\u0072\u004b\u004c\u0068\u006d\u0058\u0078\u0033\u0054\u0031\u0068\u0072\u0074\u005f\u0035\u0063";
            search.setKey(apiKey);
            search.setQ(queryTerm);
            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(25L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                Optional<SearchResult> opt = searchResultList.stream().findFirst();
                if (opt.isPresent())
                    responseUrl = "https://www.youtube.com/watch?v=" + opt.get().getId().getVideoId();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "You're being rate-limited by the YouTube API, please try again later";
        }
        return responseUrl;
    }

    @Override
    public String getHelp() {
        return "Searchs YouTube for the specified query\n`" + Core.PREFIX + getInvoke() + " <query>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "youtube";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"yt", "searchyt", "ytsearch"};
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
