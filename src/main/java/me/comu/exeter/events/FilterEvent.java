package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.AutoMuteCommand;
import me.comu.exeter.commands.moderation.FilterCommand;
import me.comu.exeter.commands.moderation.SetMuteRoleCommand;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class FilterEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (FilterCommand.isActive()) {
            if (event.getMember() == null) {
                return;
            }
            List<Role> collect = event.getMember().getRoles().stream().filter(role -> FilterCommand.filteredRoles.containsKey(role.getId())).collect(Collectors.toList());
            for (Role x : event.getMember().getRoles()) {
                if (collect.contains(x))
                    return;
            }
            if (event.getMember().getIdLong() != Core.OWNERID && !event.getMember().getId().equals(event.getJDA().getSelfUser().getId()) && !FilterCommand.filteredUsers.containsKey(event.getMember().getId())) {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().getId().equals("439205512425504771") || event.getMember().getId().equals("155149108183695360")) {
                    String message = event.getMessage().getContentRaw();
                    if (message.contains(".gg/")) {
                        if (AutoMuteCommand.active) {
                            if (!AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                AutoMuteCommand.users.put(event.getMember().getId(), 1);
                            }
                            if (AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                int warning = AutoMuteCommand.users.get(event.getMember().getId());
                                if (warning == AutoMuteCommand.threshold) {
                                    event.getMessage().delete().reason("Sent Invite Link").queue(null, null);
                                    event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention() + ". **Enjoy your mute. (" + warning + ")**").queue();
                                    event.getGuild().addRoleToMember(event.getMember(), SetMuteRoleCommand.getMutedRole()).queue();
                                    return;
                                }
                                AutoMuteCommand.users.replace(event.getMember().getId(), warning + 1);
                                event.getMessage().delete().reason("Sent Invite Link").queue(null, null);
                                event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention() + ". **Warning #" + warning + "**").queue();
                            }
                        } else {
                            event.getMessage().delete().reason("Sent Invite Link").queue(null, null);
                            event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention()).queue();
                        }
                    }
                    if (AutoMuteCommand.active) {
                        if (event.getMessage().getMentionedMembers().size() > 4) {
                            if (!AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                AutoMuteCommand.users.put(event.getMember().getId(), 1);
                            }
                            if (AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                int warning = AutoMuteCommand.users.get(event.getMember().getId());
                                if (warning == AutoMuteCommand.threshold) {
                                    event.getMessage().delete().reason("Mass Mentioned").queue(null, null);
                                    event.getChannel().sendMessage("Mass mentions are not allowed, " + event.getMember().getAsMention() + ". **Enjoy your mute. (" + warning + ")**").queue();
                                    event.getGuild().addRoleToMember(event.getMember(), SetMuteRoleCommand.getMutedRole()).queue();
                                    return;
                                }
                                AutoMuteCommand.users.replace(event.getMember().getId(), warning + 1);
                                event.getMessage().delete().reason("Mass Mentioned").queue(null, null);
                                event.getChannel().sendMessage("Mass mentions are not allowed, " + event.getMember().getAsMention() + ". **Warning #" + warning + "**").queue();
                            }
                        }
                    } else {
                        if (event.getMessage().getMentionedMembers().size() > 4) {
                            event.getMessage().delete().reason("Mass Mentioned").queue(null, null);
                            event.getChannel().sendMessage("Mass mentions are not allowed, " + event.getMember().getAsMention()).queue();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
        if (FilterCommand.isActive()) {
            if (event.getMember() == null) {
                return;
            }
            if (event.getMember().getIdLong() != Core.OWNERID && !event.getMember().getId().equals(event.getJDA().getSelfUser().getId()) && !FilterCommand.filteredUsers.containsKey(event.getMember().getId())) {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    String message = event.getMessage().getContentRaw();
                    if (message.contains(".gg/")) {
                        if (AutoMuteCommand.active) {
                            if (!AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                AutoMuteCommand.users.put(event.getMember().getId(), 1);
                            }
                            if (AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                int warning = AutoMuteCommand.users.get(event.getMember().getId());
                                if (warning == AutoMuteCommand.threshold) {
                                    event.getMessage().delete().reason("Sent Invite Link").queue(null, null);
                                    event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention() + ". **Enjoy your mute. (" + warning + ")**").queue();
                                    event.getGuild().addRoleToMember(event.getMember(), SetMuteRoleCommand.getMutedRole()).queue();
                                    return;
                                }
                                AutoMuteCommand.users.replace(event.getMember().getId(), warning + 1);
                                event.getMessage().delete().reason("Sent Invite Link").queue(null, null);
                                event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention() + ". **Warning #" + warning + "**").queue();
                            }
                        } else {
                            event.getMessage().delete().reason("Sent Invite Link").queue(null, null);
                            event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention()).queue();
                        }
                    }
                    if (AutoMuteCommand.active) {
                        if (event.getMessage().getMentionedMembers().size() > 4) {
                            if (!AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                AutoMuteCommand.users.put(event.getMember().getId(), 1);
                            }
                            if (AutoMuteCommand.users.containsKey(event.getMember().getId())) {
                                int warning = AutoMuteCommand.users.get(event.getMember().getId());
                                if (warning == AutoMuteCommand.threshold) {
                                    event.getMessage().delete().reason("Mass Mentioned").queue(null, null);
                                    event.getChannel().sendMessage("Mass mentions are not allowed, " + event.getMember().getAsMention() + ". **Enjoy your mute. (" + warning + ")**").queue();
                                    return;
                                }
                                AutoMuteCommand.users.replace(event.getMember().getId(), warning + 1);
                                event.getMessage().delete().reason("Mass Mentioned").queue(null, null);
                                event.getChannel().sendMessage("Mass mentions are not allowed, " + event.getMember().getAsMention() + ". **Warning #" + warning + "**").queue();
                            }
                        }
                    } else {
                        if (event.getMessage().getMentionedMembers().size() > 4) {
                            event.getMessage().delete().reason("Mass Mentioned").queue(null, null);
                            event.getChannel().sendMessage("Mass mentions are not allowed, " + event.getMember().getAsMention()).queue();
                        }
                    }
                }
            }

        }
    }
}

