package me.comu.exeter.commands.music;

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

public class StopCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        TextChannel textChannel = event.getChannel();
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (audioManager.isConnected() && !voiceChannel.getMembers().contains(event.getMember())) {
            event.getChannel().sendMessage("You need to be in the same voice channel as me to stop a queue").queue();
            return;
        }
        guildMusicManager.scheduler.getQueue().clear();
        guildMusicManager.player.stopTrack();
        guildMusicManager.player.setPaused(false);
        event.getChannel().sendMessage("Stopped the queue").queue();

    }

    @Override
    public String getHelp() {
        return "Stops the music\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "stop";
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
