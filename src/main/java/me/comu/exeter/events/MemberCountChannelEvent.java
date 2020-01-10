package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.MemberCountChannelCommand;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class MemberCountChannelEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        if (MemberCountChannelCommand.isVCSet) {
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(MemberCountChannelCommand.vcID);
            voiceChannel.getManager().setName("| " + (event.getGuild().getMembers().size())).queueAfter(3, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onGuildMemberLeave(@Nonnull GuildMemberLeaveEvent event) {
        if (MemberCountChannelCommand.isVCSet) {
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(MemberCountChannelCommand.vcID);
            voiceChannel.getManager().setName("| " + (event.getGuild().getMembers().size())).queueAfter(3, TimeUnit.SECONDS);
        }
    }
}
