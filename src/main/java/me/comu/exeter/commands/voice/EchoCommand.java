package me.comu.exeter.commands.voice;

import me.comu.exeter.musicplayer.AudioHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class EchoCommand implements ICommand {

    private boolean isEchoing = false;
    private AudioManager audioManager;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        // todo: finish + add shadowplay/recording
        if (isEchoing) {
            audioManager.closeAudioConnection();
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No longer echoing.").build()).queue();
            isEchoing = false;
            return;
        }
        audioManager = event.getGuild().getAudioManager();
        AudioHandler audioHandler = new AudioHandler();
        audioManager.setSendingHandler(audioHandler);
        audioManager.setReceivingHandler(audioHandler);
        audioManager.openAudioConnection(event.getGuild().getSelfMember().getVoiceState().getChannel());
        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Now echoing.").build()).queue();
        isEchoing = true;

    }

    @Override
    public String getHelp() {
        return "Starts echoing in voice chat";
    }

    @Override
    public String getInvoke() {
        return "echo";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
