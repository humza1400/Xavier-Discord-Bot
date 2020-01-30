package me.comu.exeter.events;

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
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class AntiRaidEvent extends ListenerAdapter {

    @Override
    public void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_VOICE_CHANNEL_CREATE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_VOICE_CHANNEL_DELETE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_DELETE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_TEXT_CHANNEL_CREATE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("235148962103951360") && !id.equals("416358583220043796") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_TEXT_CHANNEL_DELETE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_DELETE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_CATEGORY_CREATE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_CATEGORY_DELETE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.CHANNEL_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.CHANNEL_CREATE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_ROLE_CREATE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.ROLE_CREATE) && auditLogEntries.get(1).getType().equals(ActionType.ROLE_CREATE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    User whitelistUser = event.getJDA().getUserById(x);
                                    if (!whitelistUser.isBot())
                                        Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        if (active) {
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                User userComu = event.getJDA().getUserById(Core.OWNERID);
                Wrapper.sendPrivateMessage(userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_ROLE_DELETE**");
                return;
            }
            event.getGuild().retrieveAuditLogs().queue((auditLogEntries) -> {
                if (auditLogEntries.get(0).getType().equals(ActionType.ROLE_DELETE) && auditLogEntries.get(1).getType().equals(ActionType.ROLE_DELETE)) {
                    String id = auditLogEntries.get(0).getUser().getId();
                    Long idLong = auditLogEntries.get(0).getUser().getIdLong();
                    if (!idLong.equals(Core.OWNERID) && !id.equals(event.getJDA().getSelfUser().getId()) && !id.equals(event.getGuild().getOwnerId()) && !id.equals("464114153616048131") && !id.equals("155149108183695360") && !id.equals("650802703949234185") && !id.equals("416358583220043796") && !id.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        Member member = event.getGuild().getMemberById(id);
                        List<Role> roles = member.getRoles();
                        String[] stringArray = new String[member.getRoles().size()];
                        List<String> strings = Arrays.asList(stringArray);
                        for (int i = 0; i < roles.size(); i++) {
                            stringArray[i] = roles.get(i).getName();
                        }
                        stringArray = strings.toArray(new String[strings.size()]);
                        for (Role role : member.getRoles()) {
                            if (role.isManaged()) {
                                role.getManager().revokePermissions(Permission.values()).queue();
                            }
                            if (!role.isManaged()) {
                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                            }
                        }
                        User userComu = event.getJDA().getUserById(Core.OWNERID);
                        User userOwner = event.getGuild().getOwner().getUser();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                        Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                        if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                            for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                    {
                                        User whitelistUser = event.getJDA().getUserById(x);
                                        if (!whitelistUser.isBot())
                                            Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
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
        // TODO: FOR ADMIN PERMISSIONS / BAN / KICK / BOT_ADD
    }
}
