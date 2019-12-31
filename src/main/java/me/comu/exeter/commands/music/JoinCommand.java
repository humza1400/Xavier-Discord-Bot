package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class JoinCommand implements ICommand
{
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

            TextChannel textChannel = event.getChannel();
            AudioManager audioManager = event.getGuild().getAudioManager();

            if (audioManager.isConnected() && audioManager.getConnectedChannel().getMembers().size() > 1) {
                textChannel.sendMessage("I'm already in a voice channel").queue();
                return;
            }

            GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
            if (!memberVoiceState.inVoiceChannel()) {
                textChannel.sendMessage("You need to be in a voice channel to request me").queue();
                return;
            }

            VoiceChannel voiceChannel = memberVoiceState.getChannel();
            Member selfMember = event.getGuild().getSelfMember();

            if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                textChannel.sendMessage(String.format("I don't have permission to join %s", voiceChannel.getName())).queue();
                return;
            }
            audioManager.openAudioConnection(voiceChannel);
            textChannel.sendMessage(String.format("Joined %s", voiceChannel.getName())).queue();


    }

    @Override
    public String getHelp() {
        return "Makes the bot join your voice channel\n`" + Core.PREFIX + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "join";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"connect"};
    }

     @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
