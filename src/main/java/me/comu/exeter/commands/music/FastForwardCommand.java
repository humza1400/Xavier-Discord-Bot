package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FastForwardCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        AudioPlayer player = guildMusicManager.player;
        if (!audioManager.isConnected()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("I'm not even connected to a voice channel bro").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(voiceChannel).getMembers().contains(event.getMember())) {
            event.getChannel().sendMessageEmbeds(Utility.embed("You need to be in the same voice channel as me to fast forward a song").build()).queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            event.getChannel().sendMessageEmbeds(Utility.embed("There is no song playing to fast forward").build()).queue();
            return;
        }
        int time;
        try {
            time = Integer.parseInt(args.get(0));

        } catch (NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid number to seek to.").build()).queue();
            return;
        }
        player.getPlayingTrack().setPosition(Utility.timeToMS(0, 0, time));
        event.getChannel().sendMessageEmbeds(Utility.embed("Fast-forwarded by " + time + " seconds").build()).queue();

    }

    @Override
    public String getHelp() {
        return "Fast-forwards the current song playing\n`" + Core.PREFIX + getInvoke() + " [seconds]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "fastforward";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ff", "seek"};
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
