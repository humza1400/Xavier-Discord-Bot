package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JoinCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        AudioManager audioManager = event.getGuild().getAudioManager();

        if (audioManager.isConnected() && Objects.requireNonNull(audioManager.getConnectedChannel()).getMembers().size() > 1) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I'm already in a voice channel").build()).queue();
            return;
        }

        GuildVoiceState memberVoiceState = Objects.requireNonNull(event.getMember()).getVoiceState();
        if (!Objects.requireNonNull(memberVoiceState).inVoiceChannel()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to be in a voice channel to request me").build()).queue();
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfMember = event.getGuild().getSelfMember();

        if (!selfMember.hasPermission(Objects.requireNonNull(voiceChannel), Permission.VOICE_CONNECT)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(String.format("I don't have permission to join %s", voiceChannel.getName())).build()).queue();
            return;
        }
        audioManager.openAudioConnection(voiceChannel);
        event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Joined %s", voiceChannel.getName())).build()).queue();
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
        return new String[]{"connect"};
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
