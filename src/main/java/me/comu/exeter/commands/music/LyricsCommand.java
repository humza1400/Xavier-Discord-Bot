package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.musicplayer.Search;
import me.comu.exeter.musicplayer.SearchResult;
import me.comu.exeter.musicplayer.WebScraper;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
//        AudioManager audioManager = event.getGuild().getAudioManager();
//        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
//        TextChannel textChannel = event.getChannel();
//        TextChannel channel = event.getChannel();
//        PlayerManager playerManager = PlayerManager.getInstance();
//        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
//        AudioPlayer player = guildMusicManager.player;
//        if (!audioManager.isConnected()) {
//            textChannel.sendMessage("I'm not even connected to a voice channel bro").queue();
//            return;
//        }
//        if (audioManager.isConnected() && !voiceChannel.getMembers().contains(event.getMember())) {
//            event.getChannel().sendMessage("You need to be in the same voice channel as me to request lyrics").queue();
//            return;
//        }
//
//        if (player.getPlayingTrack() == null) {
//            channel.sendMessage("There is no song playing to retrieve lyrics from").queue();
//            return;
//        }
//        EmbedBuilder embedBuilder = new EmbedBuilder().setFooter("Powered by Genius™","https://cdn.discordapp.com/avatars/560977501728276521/67d579fdc0373c27dcd43f02e4e490c8.png?size=256").setColor(0xFFFB00).addField(player.getPlayingTrack().getInfo().title + " by " + player.getPlayingTrack().getInfo().author,"caught gay nigger exception at me.comu.exeter.commands.music.LyricsCommand\nLine: 42\nReason: gay nigger event not set up",false);
//        textChannel.sendMessage(embedBuilder.build()).queue();
        if(args.size() > 0) {
            List<SearchResult> results = new ArrayList<>();
            /* Get input */
            StringBuilder input = new StringBuilder();
            for (String i : args) {
                input.append(i).append(" ");
            }

            /* Search Lyrics */
            try {
                results = Search.lyricsSearch(input.toString());
            } catch (IOException ex) {
                event.getChannel().sendMessage("Caught IOException").queue();
            }

            /* Get Lyrics */
            try {
                SearchResult first = results.get(0);
                String lyrics = WebScraper.getLyrics(first.getLink());

                event.getChannel().sendMessage(buildLyricsEmbed(first.getTitle(), first.getAuthor(), first.getLink(), lyrics).build()).queue();
            } catch (IndexOutOfBoundsException ioobe) {
                event.getChannel().sendMessage("No result.").queue();
            } catch (IOException ioe) {
                event.getChannel().sendMessage("Caught IOException").queue();
            }
        }
    }
    private EmbedBuilder buildLyricsEmbed (String title, String author, String link, String lyrics) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Wrapper.getRandomColor())
                .setFooter("From Genius.com", null)
                .setAuthor(title + " by " + author, link, "https://cdn.discordapp.com/avatars/560977501728276521/67d579fdc0373c27dcd43f02e4e490c8.png?size=256");

        /* Only Breakup lyrics that have sections */
        if (Pattern.compile("([\\[{])(.*?)([]}])").matcher(lyrics).find()) {
            Matcher matcher = Pattern.compile("(?<=[\\[{])(?s)(.*?)(?=\\[|\\{|$)").matcher(lyrics);
            int stringLength = 0;

            /* Songs with lyrics sections */
            while (matcher.find() && stringLength < 3500) {
                String content = matcher.group();
                stringLength += content.length();

                /* Matches section title after [ or { or anything and before ] or } */
                Matcher titleMatcher = Pattern.compile("(?<=\\[|\\{|)(?s)(.*?)(?=[]}])").matcher(content);
                String section = "N/A";
                if (titleMatcher.find())
                    section = titleMatcher.group();

                /* Matches Lyrics after ] or } */
                Matcher lyricMatcher = Pattern.compile("(?<=[]}])(?s)(.*?)(?=\\[|\\{|$)").matcher(content);
                String lyric = "N/A";
                if (lyricMatcher.find()) {
                    lyric = lyricMatcher.group();
                    if (lyric.length() > 1024) lyric = lyric.substring(0, 1000) + "...";
                }

                embed.addField(section, lyric, false);
                if (stringLength > 3500) {
                    embed.addField("There are more lyrics...", "Link: **["+title+"]("+link+")**", false);
                }
            }
        } else {
            if (lyrics.length() > 2047) {
                embed.setDescription(lyrics.substring(0, 2047)+"...");
                embed.addField("There are more lyrics...", "Link: **["+title+"]("+link+")**", false);
            } else {
                embed.setDescription(lyrics);
            }
        }

        return embed;
    }

    @Override
    public String getHelp() {
        return "Shows the lyrics of the current track\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + " `";
    }

    @Override
    public String getInvoke() {
        return "lyrics";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"lyric","songlyric","songlyrics"};
    }

     @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
