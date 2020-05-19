package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidChannelSafetyCommand;
import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AntiRaidEvent extends ListenerAdapter {

    @Override
    public void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR) && !AntiRaidChannelSafetyCommand.isActive()) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_DELETE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }
                    }
                }
            });
        } else if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR) && AntiRaidChannelSafetyCommand.isActive()) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        if (AntiRaidChannelSafetyCommand.channels.containsKey(event.getChannel().getId())) {
                            event.getChannel().createCopy().setPosition(Integer.parseInt(AntiRaidChannelSafetyCommand.channels.get(event.getChannel().getId()))).queue();
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR) && !AntiRaidChannelSafetyCommand.isActive()) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }

                    }
                }
            });
        } else if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR) && AntiRaidChannelSafetyCommand.isActive()) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        if (AntiRaidChannelSafetyCommand.channels.containsKey(event.getChannel().getId())) {
                            event.getChannel().createCopy().setNSFW(event.getChannel().isNSFW()).setSlowmode(event.getChannel().getSlowmode()).setParent(event.getChannel().getParent()).setPosition(Integer.parseInt(AntiRaidChannelSafetyCommand.channels.get(event.getChannel().getId()))).queue();
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }
                    }

                }
            });

        }
    }

    @Override
    public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR) && !AntiRaidChannelSafetyCommand.isActive()) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }

                    }
                }
            });

        } else if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR) && AntiRaidChannelSafetyCommand.isActive()) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        if (AntiRaidChannelSafetyCommand.channels.containsKey(event.getCategory().getId())) {
                            event.getCategory().createCopy().setPosition(Integer.parseInt(AntiRaidChannelSafetyCommand.channels.get(event.getCategory().getId()))).queue();
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.ROLE_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.ROLE_CREATE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!Objects.requireNonNull(whitelistUser).isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                }
                            }
                        }

                    }
                }
            });

        }
    }

    @Override
    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.ROLE_DELETE) && auditLogEntries.get(1).getType().equals(ActionType.ROLE_DELETE)) {
                    String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                    Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = Objects.requireNonNull(member).getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[0]);
                        for (Role role : member.getRoles()) {
                            if (event.getGuild().getSelfMember().canInteract(role)) {
                                if (role.isManaged() || role.isPublicRole()) {
                                    role.getManager().revokePermissions(Permission.values()).queue();
                                }
                                if (!role.isManaged()) {
                                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                }
                            }
                        }
                        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                        String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!userComu.equalsIgnoreCase(userOwner))
                            Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    {
                                        User whitelistUser = event.getJDA().getUserById(x);
                                        if (!Objects.requireNonNull(whitelistUser).isBot())
                                            Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                    }
                                }
                            }

                        }
                    }
                }
            });

        }

    }

    @Override
    public void onRoleUpdatePermissions(@Nonnull RoleUpdatePermissionsEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (event.getNewPermissions().contains(Permission.ADMINISTRATOR) || event.getNewPermissions().contains(Permission.MANAGE_SERVER) || event.getNewPermissions().contains(Permission.BAN_MEMBERS) || event.getNewPermissions().contains(Permission.KICK_MEMBERS) || event.getNewPermissions().contains(Permission.MANAGE_WEBHOOKS)) {
                event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                    if (auditLogEntries.get(0).getType().equals(ActionType.ROLE_UPDATE)) {
                        String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                        Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                        if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                            Member member = event.getGuild().getMemberById(id);
                            List<Role> roles = Objects.requireNonNull(member).getRoles();
                            String[] stringArray = new String[member.getRoles().size()];
                            List<String> strings = Arrays.asList(stringArray);
                            for (int i = 0; i < roles.size(); i++) {
                                stringArray[i] = roles.get(i).getName();
                            }
                            stringArray = strings.toArray(new String[0]);
                            for (Role role : member.getRoles()) {
                                if (event.getGuild().getSelfMember().canInteract(role)) {
                                    if (role.isManaged() || role.isPublicRole()) {
                                        role.getManager().revokePermissions(Permission.values()).queue();
                                    }
                                    if (!role.isManaged()) {
                                        event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                    }
                                }
                            }
                            event.getRole().getManager().setPermissions(event.getOldPermissions()).queue();
                            String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                            String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                            LocalDateTime now = LocalDateTime.now();
                            String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                            Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `MALICIOUS_PERMISSIONS`\nBot: " + botCheck + "\nAction Taken: `Reverted Permissions For \"" + event.getRole().getName() + "\" & Removed Roles`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                            if (!userComu.equalsIgnoreCase(userOwner))
                                Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `MALICIOUS_PERMISSIONS`\nBot: " + botCheck + "\nAction Taken: `Reverted Permissions For \"" + event.getRole().getName() + "\" & Removed Roles`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                            if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                                for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                    if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                        {
                                            User whitelistUser = event.getJDA().getUserById(x);
                                            if (!Objects.requireNonNull(whitelistUser).isBot())
                                                Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `MALICIOUS_PERMISSIONS`\nBot: " + botCheck + "\nAction Taken: `Reverted Permissions For \"" + event.getRole().getName() + "\" & Removed Roles`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                                        }
                                    }
                                }

                            }
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            List<Role> roleList = event.getRoles().stream().filter(role -> role.hasPermission(Permission.ADMINISTRATOR) || role.hasPermission(Permission.MANAGE_SERVER) || role.hasPermission(Permission.BAN_MEMBERS) || role.hasPermission(Permission.KICK_MEMBERS)).collect(Collectors.toList());
            if (!roleList.isEmpty()) {
                event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                    if (auditLogEntries.get(0).getType().equals(ActionType.MEMBER_ROLE_UPDATE)) {
                        String id = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getId();
                        Long idLong = Objects.requireNonNull(auditLogEntries.get(0).getUser()).getIdLong();
                        if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("168274283414421504") && !id.equals("155149108183695360") && !id.equals("235148962103951360") && !id.equals("242730576195354624") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                            Member member = event.getGuild().getMemberById(id);
                            List<Role> roles = Objects.requireNonNull(member).getRoles();
                            String[] stringArray = new String[member.getRoles().size()];
                            List<String> strings = Arrays.asList(stringArray);

                            for (int i = 0; i < roles.size(); i++) {
                                stringArray[i] = roles.get(i).getName();
                            }
                            stringArray = strings.toArray(new String[0]);
                            for (Role role : member.getRoles()) {
                                if (event.getGuild().getSelfMember().canInteract(role)) {
                                    if (role.isManaged() || role.isPublicRole()) {
                                        role.getManager().revokePermissions(Permission.values()).queue();
                                    }
                                    if (!role.isManaged()) {
                                        event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                    }
                                }
                            }
                            String[] stringArray2 = new String[roleList.size()];
                            List<String> strings2 = Arrays.asList(stringArray2);
                            for (int i = 0; i < roleList.size(); i++) {
                                event.getGuild().removeRoleFromMember(event.getMember(), roleList.get(i)).queue();
                                stringArray2[i] = roleList.get(i).getName();
                            }
                            stringArray2 = strings2.toArray(new String[strings.size()]);
                            String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                            String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                            LocalDateTime now = LocalDateTime.now();
                            String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                            Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nSuspect: `" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (" + event.getMember().getId() + ")`" + "\nWhen: `" + dtf.format(now) + "`" + "\nType: `MALICIOUS_MEMBER_ROLE_UPDATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nWizzer's Roles Removed: `" + Arrays.deepToString(stringArray) + "`\nSuspect's Roles Removed: `" + Arrays.deepToString(stringArray2) + "`");
                            if (!userComu.equalsIgnoreCase(userOwner))
                                Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nSuspect: `" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (" + event.getMember().getId() + ")" + "\nWhen: `" + dtf.format(now) + "`" + "\nType: `MALICIOUS_MEMBER_ROLE_UPDATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nWizzer's Roles Removed: `" + Arrays.deepToString(stringArray) + "`\nSuspect's Roles Removed: `" + Arrays.deepToString(stringArray2) + "`");
                            if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                                for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                    if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                        {
                                            User whitelistUser = event.getJDA().getUserById(x);
                                            if (!Objects.requireNonNull(whitelistUser).isBot())
                                                Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nSuspect: `" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator() + " (" + event.getMember().getId() + ")" + "\nWhen: `" + dtf.format(now) + "`" + "\nType: `MALICIOUS_MEMBER_ROLE_UPDATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nWizzer's Roles Removed: `" + Arrays.deepToString(stringArray) + "`\nSuspect's Roles Removed: `" + Arrays.deepToString(stringArray2) + "`");
                                        }
                                    }
                                }

                            }
                        }
                    }
                });
            }

        }
    }


}
