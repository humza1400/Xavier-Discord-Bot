package me.comu.exeter.events;

import me.comu.exeter.commands.admin.CreateAChannelCommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CreateAChannelEvent extends ListenerAdapter {


    @Override
    public void onGuildVoiceUpdate(@Nonnull GuildVoiceUpdateEvent event) {
        if (CreateAChannelCommand.isCacSet(event.getEntity().getGuild()) && !event.getEntity().getUser().isBot()) {
            if (event.getChannelLeft() == null && event.getChannelJoined() != null) {
                if (CreateAChannelCommand.getCacMap().containsValue(event.getChannelJoined().getId())) {
                    event.getEntity().getGuild().createCopyOfChannel(Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId())))).
                            setUserlimit(6).
                            setName(event.getEntity().getEffectiveName() + "'s Channel").
                            reason("Create-A-Channel VC").
                            queue((voiceChannel ->
                            {
                                event.getEntity().getGuild().moveVoiceMember(event.getEntity(), voiceChannel).queue();
                                CreateAChannelCommand.map.put(event.getEntity().getId(), voiceChannel.getId());
                                voiceChannel.upsertPermissionOverride(event.getEntity()).setAllow(Permission.MANAGE_CHANNEL).queue();
                                Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId())))
                                        .upsertPermissionOverride(event.getEntity())
                                        .setDeny(Permission.VOICE_CONNECT).queue();
                            }));
                }
            }
            if (event.getChannelLeft() != null && CreateAChannelCommand.map.containsValue(event.getChannelLeft().getId()) && event.getChannelLeft().getMembers().isEmpty()) {
                try {
                    CreateAChannelCommand.map.remove(event.getEntity().getId());
                    event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    Objects.requireNonNull(Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId()))).getPermissionOverride(event.getEntity())).delete().queue();
                } catch (Exception ex) {
                    Logger.getLogger().print("Tried deleting an already deleted channel");
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (CreateAChannelCommand.isCacSet(event.getEntity().getGuild()) && !event.getEntity().getUser().isBot()) {
            if (CreateAChannelCommand.getCacMap().containsValue(event.getChannelJoined().getId())) {
                event.getEntity().getGuild().createCopyOfChannel(Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId())))).
                        setUserlimit(6).
                        setName(event.getEntity().getEffectiveName() + "'s Channel").
                        reason("Create-A-Channel VC").
                        queue((voiceChannel ->
                        {
                            event.getEntity().getGuild().moveVoiceMember(event.getEntity(), voiceChannel).queue();
                            CreateAChannelCommand.map.put(event.getEntity().getId(), voiceChannel.getId());
                            voiceChannel.upsertPermissionOverride(event.getEntity()).setAllow(Permission.MANAGE_CHANNEL).queue();
                            Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId()))).upsertPermissionOverride(event.getEntity()).setDeny(Permission.VOICE_CONNECT).queue();
                        }));
            }
            if (event.getChannelLeft().getMembers().isEmpty() && CreateAChannelCommand.map.containsValue(event.getChannelLeft().getId())) {
                try {
                    CreateAChannelCommand.map.remove(event.getEntity().getId());
                    event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    Objects.requireNonNull(Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId()))).getPermissionOverride(event.getEntity())).delete().queue();
                } catch (Exception ex) {
                    Logger.getLogger().print("Tried deleting an already deleted channel");
                }
            }
        }
    }
}

