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

public class LeaveCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!audioManager.isConnected()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("I'm not even connected to a voice channel bro").build()).queue();
            return;
        }
        if (Objects.requireNonNull(voiceChannel).getMembers().size() == 1) {
            audioManager.closeAudioConnection();
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully disconnected from the voice channel").build()).queue();
            return;
        }

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You must join the same voice channel as me to disconnect me").build()).queue();
            return;
        }

        audioManager.closeAudioConnection();
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully disconnected from the voice channel").build()).queue();

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
        return new String[]{"dc", "disconnect"};
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
