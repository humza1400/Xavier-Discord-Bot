package me.comu.exeter.events;

import me.comu.exeter.util.VCTrackingManager;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;

public class VCTimeTrackingEvent extends ListenerAdapter {
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if (event.getMember().getUser().isBot())
            return;
    if (VCTrackingManager.verifyJoinedUser(event.getMember().getId()))
    {
        VCTrackingManager.getJoinedVCUsers().put(event.getMember().getId(), 0L);
    }
    VCTrackingManager.setJoinTime(event.getMember().getId(), VCTrackingManager.getJoinTime(event.getMember().getId()) + Instant.now().getEpochSecond());

    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        if (event.getMember().getUser().isBot())
            return;
        if (VCTrackingManager.verifyLeaveUser(event.getMember().getId()))
        {
            VCTrackingManager.getLeaveVCUsers().put(event.getMember().getId(), 0L);
        }
        VCTrackingManager.setLeaveTime(event.getMember().getId(), VCTrackingManager.getLeaveTime(event.getMember().getId()) + Instant.now().getEpochSecond());
    }
}


