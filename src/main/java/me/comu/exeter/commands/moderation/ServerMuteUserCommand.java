package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerMuteUserCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.VOICE_MUTE_OTHERS) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to mute a user from VC").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to mute that user").build()).queue();
            return;
        }

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to mute from VC").build()).queue();
            return;
        }
        Member member = mentionedMembers.get(0);
        if (Objects.requireNonNull(member.getVoiceState()).inVoiceChannel()) {
            if (!member.getVoiceState().isGuildMuted()) {
                member.mute(true).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed("Server muted " + member.getAsMention()).build()).queue();
            } else {
                member.mute(false).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed("Unserver muted " + member.getAsMention()).build()).queue();
            }
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not in a voice channel.").build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Server mutes a user from voice channel\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "vcmute";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"servermute", "smute"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
