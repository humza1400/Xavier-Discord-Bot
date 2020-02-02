package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class SetVolumeCommand implements ICommand {

    int volume;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        TextChannel textChannel = event.getChannel();

        if (args.isEmpty()) {
            textChannel.sendMessage("Please insert a number you would like the volume to be changed to\nCurrent volume: `" + guildMusicManager.player.getVolume() + "`").queue();
            return;
        }
        if (!audioManager.isConnected()) {
            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
            return;
        }
        if (!voiceChannel.getMembers().contains(event.getMember())) {
            event.getChannel().sendMessage("You need to be in the same voice channel as me to adjust the volume").queue();
            return;
        }
        try {
            if (Integer.parseInt(args.get(0)) > 200)
                volume = 200;
            else
                volume = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            textChannel.sendMessage("That number is either invalid or too large to change the volume to or is a floating point number (integers only)").queue();
            return;
        }
        guildMusicManager.player.setVolume(volume);
        event.getChannel().sendMessage("Volume adjusted to " + Integer.parseInt(args.get(0))).queue();

    }

    @Override
    public String getHelp() {
        return "Adjusts the volume of the music\n`" + Core.PREFIX + getInvoke() + " [volume]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\nCurrent volume: `" + volume + "`";
    }

    @Override
    public String getInvoke() {
        return "volume";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"vol"};
    }

     @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
