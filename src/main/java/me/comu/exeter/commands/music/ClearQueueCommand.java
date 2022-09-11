package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClearQueueCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!audioManager.isConnected()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("I'm not even connected to a voice channel bro.").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(voiceChannel).getMembers().contains(event.getMember())) {
            event.getChannel().sendMessageEmbeds(Utility.embed("You need to be in the same voice channel as me to clear the queue.").build()).queue();
            return;
        }

//        if (player.getPlayingTrack() == null) {
//            channel.sendMessage("The queue").queue();
//            return;
//        }
        QueueCommand.getQueue().clear();
        event.getChannel().sendMessageEmbeds(Utility.embed("Queue cleared.").build()).queue();
    }

    @Override
    public String getHelp() {
        return "Clears the music bot queue\n`" + Core.PREFIX + getInvoke() + "` \nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clearq";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"clearqueue", "queueclear", "qclear"};
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
