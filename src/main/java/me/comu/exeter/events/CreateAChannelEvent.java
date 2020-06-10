package me.comu.exeter.events;

import me.comu.exeter.commands.admin.CreateAChannelCommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CreateAChannelEvent extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
/*        if (CreateAChannelCommand.isSet)
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
        }*/
    }


    @Override
    public void onGuildVoiceUpdate(@Nonnull GuildVoiceUpdateEvent event) {
        if (CreateAChannelCommand.isSet && !event.getEntity().getUser().isBot()) {
            if (event.getChannelLeft() == null && event.getChannelJoined() != null) {
                if (event.getChannelJoined().getId().equalsIgnoreCase(CreateAChannelCommand.channelID)) {
                    Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).createCopyOfChannel(Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).getVoiceChannelById(CreateAChannelCommand.channelID))).
                            setUserlimit(6).
                            setName(event.getEntity().getEffectiveName() + "'s Channel").
                            reason("Create-A-Channel VC").
                            queue((voiceChannel ->
                            {
                                Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).moveVoiceMember(event.getEntity(), voiceChannel).queue();
                                CreateAChannelCommand.map.put(event.getEntity().getId(), voiceChannel.getId());
                                voiceChannel.upsertPermissionOverride(event.getEntity()).setAllow(Permission.MANAGE_CHANNEL).queue();
                                Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).getVoiceChannelById(CreateAChannelCommand.channelID)).upsertPermissionOverride(event.getEntity()).setDeny(Permission.VOICE_CONNECT).queue();
                            }));
                }
            }
            if (event.getChannelLeft() != null && CreateAChannelCommand.map.containsValue(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().isEmpty()) {
                try {
                    CreateAChannelCommand.map.remove(event.getEntity().getId());
                    event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    Objects.requireNonNull(event.getJDA().getVoiceChannelById(CreateAChannelCommand.channelID)).putPermissionOverride(event.getEntity()).reset().queue();
                } catch (Exception ex) {
                    Logger.getLogger().print("Tried deleting an already deleted channel");
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (CreateAChannelCommand.isSet && !event.getEntity().getUser().isBot()) {
            if (event.getChannelJoined().getId().equalsIgnoreCase(CreateAChannelCommand.channelID)) {
                Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).createCopyOfChannel(Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).getVoiceChannelById(CreateAChannelCommand.channelID))).
                        setUserlimit(6).
                        setName(event.getEntity().getEffectiveName() + "'s Channel").
                        reason("Create-A-Channel VC").
                        queue((voiceChannel ->
                        {
                            Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).moveVoiceMember(event.getEntity(), voiceChannel).queue();
                            CreateAChannelCommand.map.put(event.getEntity().getId(), voiceChannel.getId());
                            voiceChannel.upsertPermissionOverride(event.getEntity()).setAllow(Permission.MANAGE_CHANNEL).queue();
                            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(CreateAChannelCommand.guildID)).getVoiceChannelById(CreateAChannelCommand.channelID)).upsertPermissionOverride(event.getEntity()).setDeny(Permission.VOICE_CONNECT).queue();
                        }));
            }
            if (event.getChannelLeft().getMembers().isEmpty() && CreateAChannelCommand.map.containsValue(event.getChannelLeft().getId())) {
                try {
                    CreateAChannelCommand.map.remove(event.getEntity().getId());
                    event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    Objects.requireNonNull(event.getJDA().getVoiceChannelById(CreateAChannelCommand.channelID)).putPermissionOverride(event.getEntity()).reset().queue();
                } catch (Exception ex) {
                    Logger.getLogger().print("Tried deleting an already deleted channel");
                }
            }
        }
    }
}

