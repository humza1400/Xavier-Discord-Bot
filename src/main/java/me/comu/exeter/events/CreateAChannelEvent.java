package me.comu.exeter.events;

import me.comu.exeter.commands.admin.CreateAChannelCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.PermOverrideManager;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
import sun.net.ftp.FtpDirEntry;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class CreateAChannelEvent extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if (CreateAChannelCommand.isSet)
        {
            if (event.getChannelJoined().getId().equalsIgnoreCase(CreateAChannelCommand.channelID)) {
                event.getGuild().createCopyOfChannel(event.getGuild().getVoiceChannelById(CreateAChannelCommand.channelID)).
                        setUserlimit(6).
                        setName(event.getMember().getEffectiveName() + "'s Channel").
                        reason("Create-A-Channel VC").
                        queue((voiceChannel ->
                {
                    event.getGuild().moveVoiceMember(event.getMember(), voiceChannel).queue();
                    CreateAChannelCommand.map.put(event.getMember().getId(), voiceChannel.getId());
                    voiceChannel.upsertPermissionOverride(event.getMember()).setAllow(Permission.MANAGE_CHANNEL).queue();
                    event.getGuild().getVoiceChannelById(CreateAChannelCommand.channelID).upsertPermissionOverride(event.getMember()).setDeny(Permission.VOICE_CONNECT).queue();
                }));
            }
        }
    }


    @Override
    public void onGuildVoiceUpdate(@Nonnull GuildVoiceUpdateEvent event) {
        if (CreateAChannelCommand.isSet) {
            if (event.getChannelLeft() != null && CreateAChannelCommand.map.containsValue(event.getChannelLeft().getId())) {
                if (event.getChannelLeft() != null && event.getChannelLeft().getMembers().isEmpty()) {
                    try {
                        CreateAChannelCommand.map.remove(event.getEntity().getId());
                        event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                        event.getJDA().getVoiceChannelById(CreateAChannelCommand.channelID).putPermissionOverride(event.getEntity()).setAllow(Permission.VOICE_CONNECT).queue();
                    } catch (Exception ex) {
                        Logger.getLogger().print("Tried deleting an already deleted channel");
                    }
                }

            }
        }
    }
}
