package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel textChannel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (voiceChannel.getMembers().size() == 1)
        {
            audioManager.closeAudioConnection();
            textChannel.sendMessage("Successfully disconnected from the voice channel").queue();
            return;
        }

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            textChannel.sendMessage("You must join the same voice channel as me to disconnect me").queue();
            return;
        }

        audioManager.closeAudioConnection();

        textChannel.sendMessage("Successfully disconnected from the voice channel").queue();

    }

    @Override
    public String getHelp() {
        return "Makes the bot leave your voice channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "leave";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"dc","disconnect"};
    }

     @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
