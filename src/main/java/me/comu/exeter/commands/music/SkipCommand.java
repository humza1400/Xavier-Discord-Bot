package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.musicplayer.TrackScheduler;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SkipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = guildMusicManager.scheduler;
        AudioPlayer player = guildMusicManager.player;
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!audioManager.isConnected()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("I'm not even connected to a voice channel bro").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(voiceChannel).getMembers().contains(event.getMember())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to be in the same voice channel as me to skip a song").build()).queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There is no song playing to skip").build()).queue();
            return;
        }
        scheduler.nextTrack();
        event.getChannel().sendMessageEmbeds(Utility.embed("**Skipped song!**").build()).queue();
        if (player.getPlayingTrack() != null)
            event.getChannel().sendMessageEmbeds(Utility.embed("Now playing **" + player.getPlayingTrack().getInfo().title + "**").build()).queue();
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
        return new String[]{"sk"};
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
