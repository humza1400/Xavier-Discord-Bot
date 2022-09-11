package me.comu.exeter.commands.music;

import com.google.gson.JsonElement;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class LastFMCommand implements ICommand {

    static final String BASE = "http://ws.audioscrobbler.com/2.0/";
    static final String ENDING = "&api_key=d53d7e288de0dcab4580ce3f47d77c2b&format=json";
    static final String GET_ALBUMS = "?method=user.gettopalbums&user=";
    static final String GET_LIBRARY = "?method=library.getartists&user=";
    static final String GET_USER = "?method=user.getinfo&user=";
    static final String RECENT_TRACKS = "?method=user.getrecenttracks";
    static final String GET_NOW_PLAYING = RECENT_TRACKS + "&limit=1&user=";
    static final String GET_ALL = RECENT_TRACKS + "&limit=1000&user=";
    static final String GET_ARTIST = "?method=user.gettopartists&user=";
    static final String GET_TRACKS = "?method=album.getinfo&username=";
    static final String GET_TRACK_INFO = "?method=track.getInfo&username=";
    static final String GET_TOP_TRACKS = "?method=user.gettoptracks&user=";
    static final String GET_CORRECTION = "?method=artist.getcorrection&artist=";
    static final String GET_ARTIST_ALBUMS = "?method=artist.gettopalbums&artist=";
    static final String GET_ARTIST_INFO = "?method=artist.getinfo&artist=";
    static final String GET_ARTIST_INFO_MBID = "?method=artist.getinfo&mbid=";
    static final String GET_TRACK_TAGS = "?method=track.gettoptags";
    static final String GET_ALBUM_TAGS = "?method=album.gettoptags";
    static final String GET_ARTIST_TAGS = "?method=artist.gettoptags";
    static final String GET_TAG_INFO = "?method=tag.getinfo&tag=";
    private final static String apiKey = "d53d7e288de0dcab4580ce3f47d77c2b";
    public static Map<String, String> fmUsers = new HashMap<>();


    /*
    todo:  userInfo, trackInfo, artist info, album info, album plays, song plays, collage
     */
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed(getHelp()).build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("set") || args.get(0).equalsIgnoreCase("setuser") || args.get(0).equalsIgnoreCase("setusername")) {
            if (args.size() < 2) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Please specify your last.fm username").build()).queue();
                return;
            }
            try {
                JsonElement jsonFromURL = Utility.getJsonFromURL(BASE + GET_USER + args.get(1) + ENDING);
                if (jsonFromURL == null || jsonFromURL.toString().contains("User not found")) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user: " + MarkdownUtil.bold(Utility.removeMentionsAndMarkdown(args.get(1)))).build()).queue();
                } else {
                    String username = jsonFromURL.getAsJsonObject().get("user").getAsJsonObject().get("name").toString();
                    fmUsers.put(event.getAuthor().getId(), username.replaceAll("\"", ""));
                    event.getChannel().sendMessageEmbeds(Utility.embed(username.replaceAll("\"", "") + ", you are good to go!").build()).queue();
                }
            } catch (IOException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(ex.toString()).build()).queue();
            }
            return;
        }
        if (args.get(0).equalsIgnoreCase("whoknows") || args.get(0).equalsIgnoreCase("wk")) {
            try {
                if (args.size() >= 2) {
                    StringJoiner stringJoiner = new StringJoiner(" ");
                    args.stream().skip(1).forEach(stringJoiner::add);
                    String MBID = getArtist(stringJoiner.toString());
                    if (MBID == null) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find that artist, sorry!").build()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(whoKnowsLeaderboard(event.getGuild(), event.getAuthor(), MBID, "search").build()).queue();
                } else {
                    if (!isAccountLinked(event.getAuthor().getId())) {
                        event.getChannel().sendMessageEmbeds(Utility.embed("Please link your last.fm account before using any commands").build()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(whoKnowsLeaderboard(event.getGuild(), event.getAuthor(), null, "recent").build()).queue();
                }
            } catch (IOException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(ex.toString()).build()).queue();
            }
        }
    }

    private boolean isAccountLinked(String id) {
        return fmUsers.containsKey(id);
    }

    private String getArtist(String searchTerm) {
        String mbid = null;
        try {
            JsonElement jsonElement = Utility.getJsonFromURL(BASE + GET_ARTIST_INFO + searchTerm.replaceAll(" ", "%20") + "&autocorrect=1" + ENDING);
            mbid = jsonElement.getAsJsonObject().get("artist").getAsJsonObject().get("mbid").toString().replaceAll("\"", "");
        } catch (NullPointerException | IOException ex) {
            ex.printStackTrace();
        }
        return mbid;
    }

    private EmbedBuilder whoKnowsLeaderboard(Guild guild, User author, String MBID, String type) throws IOException {
        String user = fmUsers.get(author.getId());
        JsonElement jsonFromURL;
        String name = "null";
        if (MBID == null && type.equalsIgnoreCase("recent")) {
            // for some reason GET_NOW_PLAYING returns an incorrect MBID, so use artist name for getting plays
            jsonFromURL = Utility.getJsonFromURL(BASE + GET_NOW_PLAYING + user + ENDING);
            MBID = jsonFromURL.getAsJsonObject().get("recenttracks").getAsJsonObject().get("track").getAsJsonArray().get(0).getAsJsonObject().get("mbid").toString().replaceAll("\"","");
            name = jsonFromURL.getAsJsonObject().get("recenttracks").getAsJsonObject().get("track").getAsJsonArray().get(0).getAsJsonObject().get("artist").getAsJsonObject().get("#text").toString().replaceAll("\"", "");
        } else if (MBID != null && type.equalsIgnoreCase("search")) {
            jsonFromURL = Utility.getJsonFromURL(BASE + GET_ARTIST_INFO_MBID + MBID + "&username=" + user + ENDING);
            name = jsonFromURL.getAsJsonObject().get("artist").getAsJsonObject().get("name").toString().replaceAll("\"", "");
        }
        int plays = getPlays(user, MBID);
        StringBuilder stringBuffer = new StringBuilder();
        HashMap<String, Integer> playsMap = new HashMap<>();
        for (String entry : fmUsers.keySet()) {
            Member member = guild.getMemberById(entry);
            if (member != null) {
                playsMap.put(member.getUser().getAsTag(), getPlays(user, MBID));
            }
        }
        LinkedHashMap<String, Integer> collectPlays = playsMap.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        int counter = 0;
        for (Map.Entry<String, Integer> entry : collectPlays.entrySet()) {
            if (counter == 0) {
                stringBuffer.append("`\uD83D\uDC51` ").append("[").append(entry.getKey()).append("](https://www.last.fm/user/").append(user).append(") \u2014 **").append(entry.getValue()).append("** plays");
            } else {
                stringBuffer.append("`").append(counter + 1).append(".` [").append(entry.getKey()).append("](https://www.last.fm/user/").append(user).append(") \u2014 **").append(entry.getValue()).append("** plays");
            }
            counter++;
            if (counter == 9) {
                break;
            }
            stringBuffer.append("\n");
        }
        if (stringBuffer.toString().equalsIgnoreCase("")) {
            stringBuffer.append("No users have linked their last.fm account :(");
        }
        int pos = new ArrayList<>(collectPlays.keySet()).indexOf(author.getAsTag());
        return Utility.embedMessage(stringBuffer.toString()).setTitle("Who knows " + name).setColor(Core.getInstance().getColorTheme()).setFooter(pos == -1 ? "Link your account using " + Core.PREFIX + "lfm set <user>" : "You are ranked #" + ++pos + " with " + plays + " plays");
    }

    private int getPlays(String user, String artist) {
        int plays = -1;
        try {
            JsonElement jsonFromURL = Utility.getJsonFromURL(BASE + GET_ARTIST_INFO_MBID + artist + "&username=" + user + ENDING);
            return Integer.parseInt(jsonFromURL.getAsJsonObject().get("artist").getAsJsonObject().get("stats").getAsJsonObject().get("userplaycount").toString().replaceAll("\"", ""));
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return plays;
    }

    private Track getTrackInfo(String username, String artist, String trackName) {
        String url = BASE + GET_TRACK_INFO + username + "&artist=" + URLEncoder.encode(artist, StandardCharsets.UTF_8) + "&track=" + URLEncoder.encode(trackName, StandardCharsets.UTF_8) + apiKey + ENDING + "&autocorrect=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36").build();
        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = Objects.requireNonNull(response.body()).string();
            JSONObject obj = new JSONObject(jsonResponse);
            System.out.println(jsonResponse);
            obj = obj.getJSONObject("track");
            int userplaycount = 0;
            if (obj.has("userplaycount")) {
                userplaycount = obj.getInt("userplaycount");
            }
            String reTrackName = obj.getString("name");
            boolean userloved = false;
            if (obj.has("userloved")) {
                userloved = obj.getInt("userloved") != 0;
            }
            int duration = obj.getInt("duration");
            String reArtist = obj.getJSONObject("artist").getString("name");

            Track track = new Track(reArtist, reTrackName, userplaycount, userloved, duration);

            JSONObject images;
            if ((images = obj).has("album") && (images = images.getJSONObject("album")).has("image")) {
                JSONArray ar = images.getJSONArray("image");
                track.setImageUrl(ar.getJSONObject(ar.length() - 1).getString("#text"));
            }
            return track;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String getHelp() {
        return "LastFM Integration:\n`" + Core.PREFIX + getInvoke() + " set <username>`\n`" +  Core.PREFIX + getInvoke() + " wk <artist>`\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lastfm";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"lfm", "fm"};
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

class Track {
    private final String ARTIST;
    private final String TRACK_NAME;
    private String IMAGEURL;
    private final boolean USERLOVED;
    private final int USERPLAYCOUNT;
    private final int DURATION;

    Track(String ARTIST, String TRACK_NAME, int USERPLAYCOUNT, boolean USERLOVED, int DURATION) {
        this.ARTIST = ARTIST;
        this.TRACK_NAME = TRACK_NAME;
        this.USERPLAYCOUNT = USERPLAYCOUNT;
        this.USERLOVED = USERLOVED;
        this.DURATION = DURATION;
        this.IMAGEURL = null;
    }

    Track(String ARTIST, String TRACK_NAME, int USERPLAYCOUNT, boolean USERLOVED, int DURATION, String IMAGEURL) {
        this.ARTIST = ARTIST;
        this.TRACK_NAME = TRACK_NAME;
        this.USERPLAYCOUNT = USERPLAYCOUNT;
        this.USERLOVED = USERLOVED;
        this.DURATION = DURATION;
        this.IMAGEURL = IMAGEURL;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public String getTRACK_NAME() {
        return TRACK_NAME;
    }

    public int getUSERPLAYCOUNT() {
        return USERPLAYCOUNT;
    }

    public boolean getUSERLOVED() {
        return USERLOVED;
    }

    public int getDURATION() {
        return DURATION;
    }

    public String getIMAGEURL() {
        return IMAGEURL;
    }

    public void setImageUrl(String imageURL) {
        this.IMAGEURL = imageURL;
    }

    @Override
    public String toString() {
        return "Artist: " + getARTIST() + " | Track Name: " + getTRACK_NAME() + " | Play Count: " + getUSERPLAYCOUNT() + " | Loved: " + getUSERLOVED() + " | Duration: " + getDURATION() + " | Image URL: " + getIMAGEURL();
    }
}