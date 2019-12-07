package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class PauseCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());

        guildMusicManager.player.setPaused(true);
        event.getChannel().sendMessage("Paused the queue").queue();

    }

    @Override
    public String getHelp() {
        return "Pauses the music\n`" + Core.PREFIX + getInvoke() + "`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "pause";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
