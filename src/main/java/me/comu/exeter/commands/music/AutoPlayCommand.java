package me.comu.exeter.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AutoPlayCommand implements ICommand {

    public static final List<String> autoPlayGuilds = new ArrayList<>();
    private final YouTube youTube;

    public AutoPlayCommand() {
        YouTube temp = null;
        try {
            temp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null).setApplicationName("Exeter Discord Bot").build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        youTube = temp;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String relatedVideoID = null;
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        TextChannel textChannel = event.getChannel();
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (!Objects.requireNonNull(voiceChannel).getMembers().contains(event.getMember())) {
            event.getChannel().sendMessage("You need to be in the same voice channel as me to enable auto-play").queue();
            return;
        }
        if (autoPlayGuilds.contains(event.getGuild().getId())) {
            autoPlayGuilds.remove(event.getGuild().getId());
            event.getChannel().sendMessage("Auto-Play has been **disabled**").queue();
        } else {
            if (guildMusicManager.player.getPlayingTrack() == null) {
                event.getChannel().sendMessage("You need to be playing a track to turn auto-play on").queue();
                return;
            }
            AudioTrackInfo info = guildMusicManager.player.getPlayingTrack().getInfo();
            relatedVideoID = Utility.getYouTubeId(info.uri);
            System.out.println("Related video id " + relatedVideoID);
            autoPlayGuilds.add(event.getGuild().getId());
            event.getChannel().sendMessage("Auto-Play has been **enabled**").queue();
        }
        List<String> videos = searchYoutube(relatedVideoID);
        if (videos == null)
        {
            event.getChannel().sendMessage("Unfortunately we're being rate limited by youtube and cannot use auto-play at this moment").queue();
            autoPlayGuilds.remove(event.getGuild().getId());
        }
        // https://developers.google.com/youtube/v3/guides/implementation/videos
        // https://developers.google.com/youtube/v3/docs/search/list?apix_params=%7B%22part%22%3A%22snippet%22%2C%22type%22%3A%22video%22%2C%22relatedToVideoId%22%3A%22wtLJPvx7-ys%22%7D&apix=true
    }

    @Nullable
    private List<String> searchYoutube(String youtubeId) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setType("video")
                    .setPart("snippet")
                    .setRelatedToVideoId(youtubeId)
                    .setKey("\u0041\u0049\u007a\u0061\u0053\u0079\u0041\u006c\u0073\u0039\u007a\u0072\u0056\u0056\u0051\u0074\u005a\u006b\u0073\u006d\u002d\u0074\u004d\u0072\u004b\u004c\u0068\u006d\u0058\u0078\u0033\u0054\u0031\u0068\u0072\u0074\u005f\u0035\u0063")
                    .execute().getItems();
            if (!results.isEmpty()) {
                List<String> songUrls = new ArrayList<>();
                for (SearchResult result : results) {
                    songUrls.add("https://www.youtube.com/watch?v=" + result.getId().getVideoId());
                }
                System.out.println("Results: " + results);
                System.out.println("Song Urls: " + songUrls);
                return songUrls;
            } else {
                System.out.println("empty");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public String getHelp() {
        return "Clears the music bot queue\n`" + Core.PREFIX + getInvoke() + "` \nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "autoplay";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ap"};
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
