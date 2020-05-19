package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.MemberCountChannelCommand;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MemberCountChannelEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        try {
            if (MemberCountChannelCommand.isVCSet && !MemberCountChannelCommand.isMessageSet) {
                VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(MemberCountChannelCommand.vcID);
                Objects.requireNonNull(voiceChannel).getManager().setName("| " + (event.getGuild().getMembers().size())).queueAfter(3, TimeUnit.SECONDS);
            } else if (MemberCountChannelCommand.isVCSet) {
                VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(MemberCountChannelCommand.vcID);
                Objects.requireNonNull(voiceChannel).getManager().setName(MemberCountChannelCommand.message.replace("{$count}", Integer.toString(event.getGuild().getMemberCount()))).queueAfter(3, TimeUnit.SECONDS);
            }
        } catch (NullPointerException ex) {
            MemberCountChannelCommand.isVCSet = false;
        }
    }


    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        try {
            if (MemberCountChannelCommand.isVCSet && !MemberCountChannelCommand.isMessageSet) {
                VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(MemberCountChannelCommand.vcID);
                Objects.requireNonNull(voiceChannel).getManager().setName("| " + (event.getGuild().getMembers().size())).queueAfter(3, TimeUnit.SECONDS);
            } else if (MemberCountChannelCommand.isVCSet) {
                VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(MemberCountChannelCommand.vcID);
                Objects.requireNonNull(voiceChannel).getManager().setName(MemberCountChannelCommand.message.replace("{$count}", Integer.toString(event.getGuild().getMemberCount()))).queueAfter(3, TimeUnit.SECONDS);
            }
        } catch (NullPointerException ex) {
            MemberCountChannelCommand.isVCSet = false;
        }
    }

}
