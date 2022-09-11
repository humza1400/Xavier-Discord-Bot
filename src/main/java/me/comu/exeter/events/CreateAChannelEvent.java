package me.comu.exeter.events;

import me.comu.exeter.commands.admin.CreateAChannelCommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class CreateAChannelEvent extends ListenerAdapter {


    @Override
    public void onGuildVoiceUpdate(@Nonnull GuildVoiceUpdateEvent event) {
        if (CreateAChannelCommand.isCacSet(event.getEntity().getGuild()) && !event.getEntity().getUser().isBot()) {
            if (event.getChannelLeft() == null && event.getChannelJoined() != null) {
                if (CreateAChannelCommand.getGuildCacMap().containsValue(event.getChannelJoined().getId())) {
                    event.getEntity().getGuild().createCopyOfChannel(Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getGuildCacMap().get(event.getEntity().getGuild().getId())))).
                            setUserlimit(null).
                            setName(event.getEntity().getEffectiveName() + "'s Channel").
                            reason("Create-A-Channel VC").
                            queue((voiceChannel ->
                            {
                                event.getEntity().getGuild().moveVoiceMember(event.getEntity(), voiceChannel).queue();
                                CreateAChannelCommand.vcMap.put(event.getEntity().getId(), voiceChannel.getId());
//                                voiceChannel.upsertPermissionOverride(event.getEntity()).setAllow(Permission.MANAGE_CHANNEL).queue();
/*                                Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId())))
                                        .upsertPermissionOverride(event.getEntity())
                                        .setDeny(Permission.VOICE_CONNECT).queue();*/
                            }));
                }
            }
            if (event.getChannelLeft() != null && CreateAChannelCommand.vcMap.containsValue(event.getChannelLeft().getId())) {
                if (event.getChannelLeft().getMembers().isEmpty()) {
                    try {
                        CreateAChannelCommand.vcMap.remove(event.getEntity().getId());
                        event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    } catch (Exception ex) {
                        Logger.getLogger().print("Tried deleting an already deleted channel");
                        ex.printStackTrace();
                    }
                } else if (event.getChannelLeft().getMembers().size() == 1) {
                    Member member = event.getChannelLeft().getMembers().get(0);
                    if (member.getUser().isBot()) {
                        try {
                            CreateAChannelCommand.vcMap.remove(Utility.getKeyByValue(CreateAChannelCommand.vcMap, event.getChannelLeft().getId()));
                            event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                        } catch (Exception ex) {
                            Logger.getLogger().print("Tried deleting an already deleted channel");
                            ex.printStackTrace();
                        }
                    } else {
                        CreateAChannelCommand.vcMap.remove(event.getMember().getId());
                        CreateAChannelCommand.vcMap.put(member.getId(), event.getChannelLeft().getId());
                    }
                } else if (CreateAChannelCommand.vcMap.containsKey(event.getMember().getId())) {
                    List<Member> members = event.getChannelLeft().getMembers();
                    for (Member member : members) {
                        if (!member.getUser().isBot()) {
                            CreateAChannelCommand.vcMap.remove(event.getMember().getId());
                            CreateAChannelCommand.vcMap.put(member.getId(), event.getChannelLeft().getId());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (CreateAChannelCommand.isCacSet(event.getEntity().getGuild()) && !event.getEntity().getUser().isBot()) {
            if (CreateAChannelCommand.getGuildCacMap().containsValue(event.getChannelJoined().getId())) {
                event.getEntity().getGuild().createCopyOfChannel(Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getGuildCacMap().get(event.getEntity().getGuild().getId())))).
                        setUserlimit(null).
                        setName(event.getEntity().getEffectiveName() + "'s Channel").
                        reason("Create-A-Channel VC").
                        queue((voiceChannel ->
                        {
                            event.getEntity().getGuild().moveVoiceMember(event.getEntity(), voiceChannel).queue();
                            CreateAChannelCommand.vcMap.put(event.getEntity().getId(), voiceChannel.getId());
//                            voiceChannel.upsertPermissionOverride(event.getEntity()).setAllow(Permission.MANAGE_CHANNEL).queue();
//                            Objects.requireNonNull(event.getEntity().getGuild().getVoiceChannelById(CreateAChannelCommand.getCacMap().get(event.getEntity().getGuild().getId())))
//                                    .upsertPermissionOverride(event.getEntity())
//                                    .setDeny(Permission.VOICE_CONNECT).queue();
                        }));
            }
            if (CreateAChannelCommand.vcMap.containsValue(event.getChannelLeft().getId())) {
                if (event.getChannelLeft().getMembers().isEmpty()) {
                    try {
                        CreateAChannelCommand.vcMap.remove(event.getEntity().getId());
                        event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    } catch (Exception ex) {
                        Logger.getLogger().print("Tried deleting an already deleted channel");
                        ex.printStackTrace();
                    }
                }
            } else if (event.getChannelLeft().getMembers().size() == 1) {
                Member member = event.getChannelLeft().getMembers().get(0);
                if (member.getUser().isBot()) {
                    try {
                        CreateAChannelCommand.vcMap.remove(Utility.getKeyByValue(CreateAChannelCommand.vcMap, event.getChannelLeft().getId()));
                        event.getChannelLeft().delete().reason("Create-A-Channel VC").queue();
                    } catch (Exception ex) {
                        Logger.getLogger().print("Tried deleting an already deleted channel");
                        ex.printStackTrace();
                    }
                }
            } else if (CreateAChannelCommand.vcMap.containsKey(event.getMember().getId())) {
                List<Member> members = event.getChannelLeft().getMembers();
                for (Member member : members) {
                    if (!member.getUser().isBot()) {
                        CreateAChannelCommand.vcMap.remove(event.getMember().getId());
                        CreateAChannelCommand.vcMap.put(member.getId(), event.getChannelLeft().getId());
                        break;
                    } else {
                        CreateAChannelCommand.vcMap.remove(event.getMember().getId());
                        CreateAChannelCommand.vcMap.put(member.getId(), event.getChannelLeft().getId());
                    }
                }
            }
        }
    }
}

