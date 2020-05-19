package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class LyricsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        boolean songPlaying;
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.player;
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
        EmbedBuilder embedBuilder = new EmbedBuilder();
        EmbedBuilder embedBuilder2 = new EmbedBuilder();
        Request request = new Request.Builder().addHeader("Authorization", "Bearer -l0QGGg7DvRtR3WuHqo41X8igda4a4HPUMdVCqaxhXIQjIcYrGLfBUhwxHdjMmqV").url(searchUrl).build();
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
                    String result = jsonObject.getJSONObject("response").getJSONArray("hits").get(0).toString();
                    JSONObject jsonObject2 = new JSONObject(result);
                    String songID = jsonObject2.getJSONObject("result").toMap().get("id").toString();
                    final String songSearch = "https://api.genius.com/songs/" + songID;
                    System.out.println("Making a request to: " + songSearch);
                    Request request = new Request.Builder().addHeader("Authorization", "Bearer -l0QGGg7DvRtR3WuHqo41X8igda4a4HPUMdVCqaxhXIQjIcYrGLfBUhwxHdjMmqV").url(songSearch).build();
                    httpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String jsonResponse = Objects.requireNonNull(response.body()).string();
                                Logger.getLogger().print(jsonResponse + "\n");
                                JSONObject jsonObject = new JSONObject(jsonResponse);
                                String song_thumbnail = jsonObject.getJSONObject("response").getJSONObject("song").toMap().get("song_art_image_thumbnail_url").toString();
                                String full_title = jsonObject.getJSONObject("response").getJSONObject("song").toMap().get("full_title").toString();
                                if (songPlaying) {
                                    embedBuilder.setThumbnail(song_thumbnail).setFooter("Powered by Genius\u2122", "https://images.genius.com/8ed669cadd956443e29c70361ec4f372.1000x1000x1.png").setColor(0xFFFB00).addField(player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author, "", false);
                                    embedBuilder2.setThumbnail(song_thumbnail).setFooter("Powered by Genius\u2122", "https://images.genius.com/8ed669cadd956443e29c70361ec4f372.1000x1000x1.png").setColor(0xFFFB00).addField(player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author, "", false);
                                } else {
                                    embedBuilder.setThumbnail(song_thumbnail).setFooter("Powered by Genius\u2122", "https://images.genius.com/8ed669cadd956443e29c70361ec4f372.1000x1000x1.png").setColor(0xFFFB00).addField(full_title, "", false);
                                    embedBuilder2.setThumbnail(song_thumbnail).setFooter("Powered by Genius\u2122", "https://images.genius.com/8ed669cadd956443e29c70361ec4f372.1000x1000x1.png").setColor(0xFFFB00).addField(full_title, "", false);
                                }
                                final String songUrl = jsonObject.getJSONObject("response").getJSONObject("song").toMap().get("url").toString();
                                String lyrics;
                                Logger.getLogger().print(("Getting input stream for genius song " + songUrl));
                                try (InputStream stream = getInputStreamFor(songUrl);
                                     BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                                    lyrics = findLyrics(reader);
                                    if (formatLyrics(lyrics).length() > 2000)
                                    {
                                        final int mid = formatLyrics(lyrics).length() / 2;
                                        String[] parts = {formatLyrics(lyrics).substring(0, mid),formatLyrics(lyrics).substring(mid)};
                                        embedBuilder.setDescription(parts[0]);
                                        embedBuilder2.setDescription(parts[1]);
                                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                                        event.getChannel().sendMessage(embedBuilder2.build()).queue();
                                    } else {
                                        embedBuilder.setDescription(formatLyrics(lyrics));
                                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
            }
        });

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

    private static String formatLyrics(String lyrics) {
        return lyrics.replaceAll("(<!--/?sse-->)|(</?[apibu]>)|(\\[[^\\[\\]]+])|(<a[^>]+>)", "")
                .replaceAll("(<br>){2}(<br>)+", "<br><br>")
                .replaceAll("<br>", "\n")
                .replaceAll("<em>","")
                .replaceAll("</em>","").trim();
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
        return new String[]{"lyric", "songlyric", "songlyrics"};
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
