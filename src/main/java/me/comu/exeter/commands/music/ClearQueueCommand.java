package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class ClearQueueCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        TextChannel channel = event.getChannel();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.player;
        TextChannel textChannel = event.getChannel();
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (audioManager.isConnected() && !voiceChannel.getMembers().contains(event.getMember())) {
            event.getChannel().sendMessage("You need to be in the same voice channel as me to clear the queue").queue();
            return;
        }

//        if (player.getPlayingTrack() == null) {
//            channel.sendMessage("The queue").queue();
//            return;
//        }
        QueueCommand.getQueue().clear();
        event.getChannel().sendMessage("Queue cleared").queue();
    }

    @Override
    public String getHelp() {
        return "Clears the music bot queue\n`" + Core.PREFIX + getInvoke() + "` \nnAliases: `" + Arrays.deepToString(getAlias())+ "`";
    }

    @Override
    public String getInvoke() {
        return "clearq";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"clearqueue","queueclear","qclear"};
    }
}
