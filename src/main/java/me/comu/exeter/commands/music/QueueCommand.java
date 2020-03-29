package me.comu.exeter.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {

    private static BlockingQueue<AudioTrack> uniQueue;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        uniQueue = queue;
        if (queue.isEmpty()) {
            channel.sendMessage("The queue is empty").queue();
            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder builder = new EmbedBuilder().setTitle("Music Queue (" + queue.size() + ")").setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now());

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    "%s - %s (%s)\n",
                    info.title,
                    info.author,
                    info.length

            ));
        }

        channel.sendMessage(builder.build()).queue();

    }

    public static BlockingQueue<AudioTrack> getQueue()
    {
        return uniQueue;
    }

    @Override
    public String getHelp() {
        return "Shows the current music queue\n`" + Core.PREFIX + getInvoke() + "`\nAliases:: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "queue";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"q"};
    }

     @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
