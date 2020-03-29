package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
        String generalInfo = String.format("**Owner**: <@%s>\n**Region**: %s\n**Creation Date**: %s\n**Security Level**: %s",
                guild.getOwnerId(),
                guild.getRegion().getName(),
                guild.getTimeCreated().format(timeFormatter),
                convertVerificationLevel(guild.getVerificationLevel())
        );

        String memberInfo = String.format(
                "**Total Roles**: %s\n**Total Members**: %s\n**Online Members**: %s\n**Offline Members**: %s\n**Bot Count**: %s\n**Voice Channels**: %s\n**Text Channels**: %s\n**Categories**: %s",
                guild.getRoleCache().size(),
                guild.getMemberCache().size(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.ONLINE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.OFFLINE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getUser().isBot()).count(),
                guild.getVoiceChannels().size(),
                guild.getTextChannels().size(),
                guild.getCategories().size()
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("Information", generalInfo, false)
                .addField("Roles, Channels, & Member Counts", memberInfo, false)
                .setFooter("Requested By " + Objects.requireNonNull(event.getMember()).getUser().getAsTag(), event.getMember().getUser().getAvatarUrl());

        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Returns information about the server\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "serverinfo";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"serverinformation"};
    }

    private String convertVerificationLevel(Guild.VerificationLevel lvl) {
        String[] names = lvl.name().toLowerCase().split("_");
        StringBuilder out = new StringBuilder();

        for (String name : names) {
            out.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).append(" ");
        }

        return out.toString().trim();
    }

     @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
