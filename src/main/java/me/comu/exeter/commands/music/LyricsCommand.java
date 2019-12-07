package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class LyricsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        TextChannel textChannel = event.getChannel();
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.player;
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (audioManager.isConnected() && !voiceChannel.getMembers().contains(event.getMember())) {
            event.getChannel().sendMessage("You need to be in the same voice channel as me to request lyrics").queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("There is no song playing to retrieve lyrics from").queue();
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder().setFooter("Powered by Genius™","https://cdn.discordapp.com/avatars/560977501728276521/67d579fdc0373c27dcd43f02e4e490c8.png?size=256").setColor(0xFFFB00).addField(player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author,"caught gay nigger exception at me.comu.exeter.commands.music.LyricsCommand\nLine: 42\nReason: gay nigger event not set up",false);
        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows the lyrics of the current track\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + " `";
    }

    @Override
    public String getInvoke() {
        return "lyrics";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"lyric","songlyric","songlyrics"};
    }
}
