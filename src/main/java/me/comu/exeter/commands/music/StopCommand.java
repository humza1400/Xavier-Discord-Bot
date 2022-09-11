package me.comu.exeter.commands.music;

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

public class StopCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!audioManager.isConnected()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("I'm not even connected to a voice channel bro").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(voiceChannel).getMembers().contains(event.getMember())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to be in the same voice channel as me to stop a queue").build()).queue();
            return;
        }
        guildMusicManager.scheduler.getQueue().clear();
        guildMusicManager.player.stopTrack();
        guildMusicManager.player.setPaused(false);
        event.getChannel().sendMessageEmbeds(Utility.embed("Stopped the queue").build()).queue();

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

    @Override
    public boolean isPremium() {
        return true;
    }
}
