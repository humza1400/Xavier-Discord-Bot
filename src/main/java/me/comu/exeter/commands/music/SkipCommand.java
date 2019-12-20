package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.musicplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class SkipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = guildMusicManager.scheduler;
        AudioPlayer player = guildMusicManager.player;
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        TextChannel textChannel = event.getChannel();
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (audioManager.isConnected() && !voiceChannel.getMembers().contains(event.getMember())) {
            event.getChannel().sendMessage("You need to be in the same voice channel as me to skip a song").queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("There is no song playing to skip").queue();
            return;
        }
        scheduler.nextTrack();
        channel.sendMessage("**Skipped song!**").queue();
            if (player.getPlayingTrack() != null)
            channel.sendMessage("Now playing **" + player.getPlayingTrack().getInfo().title + "**").queue();

    }

    @Override
    public String getHelp() {
        return "Skips the current song\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "skip";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"s"};
    }
}
