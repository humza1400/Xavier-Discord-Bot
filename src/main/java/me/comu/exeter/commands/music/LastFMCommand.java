package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LastFMCommand implements ICommand {

    static final String BASE = "http://ws.audioscrobbler.com/2.0/";
    static final String GET_ALBUMS = "?method=user.gettopalbums&user=";
    static final String GET_LIBRARY = "?method=library.getartists&user=";
    static final String GET_USER = "?method=user.getinfo&user=";
    static final String ENDING = "&format=json";
    static final String RECENT_TRACKS = "?method=user.getrecenttracks";
    static final String GET_NOW_PLAYINH = RECENT_TRACKS + "&limit=1&user=";
    static final String GET_ALL = RECENT_TRACKS + "&limit=1000&user=";
    static final String GET_ARTIST = "?method=user.gettopartists&user=";
    static final String GET_TRACKS = "?method=album.getinfo&username=";
    static final String GET_TRACK_INFO = "?method=track.getInfo&username=";
    static final String GET_TOP_TRACKS = "?method=user.gettoptracks&user=";
    static final String GET_CORRECTION = "?method=artist.getcorrection&artist=";
    static final String GET_ARTIST_ALBUMS = "?method=artist.gettopalbums&artist=";
    static final String GET_ARTIST_INFO = "?method=artist.getinfo&artist=";
    static final String GET_TRACK_TAGS = "?method=track.gettoptags";
    static final String GET_ALBUM_TAGS = "?method=album.gettoptags";
    static final String GET_ARTIST_TAGS = "?method=artist.gettoptags";
    static final String GET_TAG_INFO = "?method=tag.getinfo&tag=";
    private final String apiKey = "c8b284109f672342bb4752ac186a9a40";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        System.out.println(getTrackInfo(args.get(0), args.get(1), args.get(2)));
    }

    public Track getTrackInfo(String username, String artist, String trackName) {
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
        return "LastFM shit\n`" + Core.PREFIX + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lastfm";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
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

    public void setImageUrl(String imageURL)
    {
        this.IMAGEURL = imageURL;
    }

    @Override
    public String toString() {
        return "Artist: " + getARTIST() + " | Track Name: " + getTRACK_NAME() + " | Play Count: " + getUSERPLAYCOUNT() + " | Loved: " + getUSERLOVED() + " | Duration: " + getDURATION() + " | Image URL: " + getIMAGEURL();
    }
}
