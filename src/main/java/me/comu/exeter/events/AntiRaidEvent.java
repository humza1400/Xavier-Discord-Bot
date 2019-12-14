package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.wrapper.Wrapper;
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
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.CHANNEL_CREATE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.CHANNEL_CREATE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.CHANNEL_CREATE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.CHANNEL_CREATE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userAndres = event.getJDA().getUserById("620392170326851625");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
            }
        }
    }

    @Override
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.CHANNEL_DELETE) && event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.CHANNEL_DELETE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.CHANNEL_DELETE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.CHANNEL_DELETE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userAndres = event.getJDA().getUserById("620392170326851625");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `VOICE_CHANNEL_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
            }
        }
    }

    @Override
    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.CHANNEL_CREATE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.CHANNEL_CREATE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.CHANNEL_CREATE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.CHANNEL_CREATE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                    User userComu = event.getJDA().getUserById("175728291460808706");
                    User userAndres = event.getJDA().getUserById("620392170326851625");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                    Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                    Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CHANNEL_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");

            }
        }
    }

    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.CHANNEL_DELETE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.CHANNEL_CREATE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.CHANNEL_CREATE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.CHANNEL_DELETE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                    User userComu = event.getJDA().getUserById("175728291460808706");
                    User userAndres = event.getJDA().getUserById("620392170326851625");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                    Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                    Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `TEXT_CHANNEL_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");


            }
        }
    }

    @Override
    public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.CHANNEL_CREATE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.CHANNEL_CREATE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.CHANNEL_CREATE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.CHANNEL_CREATE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userAndres = event.getJDA().getUserById("620392170326851625");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now)  + "\nType: Ban\nAction Taken: Banned User");
                Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");


            }
        }
    }

    @Override
    public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.CHANNEL_CREATE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.CHANNEL_CREATE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.CHANNEL_CREATE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.CHANNEL_CREATE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                    User userComu = event.getJDA().getUserById("175728291460808706");
                    User userAndres = event.getJDA().getUserById("620392170326851625");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                    Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                    Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `CATEGORY_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");


            }
        }
    }

    @Override
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.ROLE_CREATE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.ROLE_CREATE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.ROLE_CREATE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.ROLE_CREATE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userAndres = event.getJDA().getUserById("620392170326851625");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_CREATE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");


            }
        }
    }

    @Override
    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
        boolean active = AntiRaidCommand.isActive();
        if (active) {
            if (event.getGuild().retrieveAuditLogs().complete().get(0).getType().equals(ActionType.ROLE_DELETE) &&event.getGuild().retrieveAuditLogs().complete().get(1).getType().equals(ActionType.ROLE_DELETE) && event.getGuild().retrieveAuditLogs().complete().get(2).getType().equals(ActionType.ROLE_DELETE)) {
                String id = event.getGuild().retrieveAuditLogs().type(ActionType.ROLE_DELETE).complete().get(0).getUser().getId();
                Member member = event.getGuild().getMemberById(id);
                List<Role> roles = member.getRoles();
                String[] stringArray = new String[member.getRoles().size()];
                List<String> strings = Arrays.asList(stringArray);
                for (int i = 0; i < roles.size(); i++)
                {
                    stringArray[i] = roles.get(i).getName();
                }
                stringArray = strings.toArray(new String[strings.size()]);
                for (Role role : member.getRoles()) {
                    event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                }
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userAndres = event.getJDA().getUserById("620392170326851625");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now) + "\nType: Ban\nAction Taken: Banned User");
                Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");
                Wrapper.sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `ROLE_DELETE`\nAction Taken: `Roles Removed`\nRoles Removed: `" + Arrays.deepToString(stringArray) + "`");


            }
        }
    }

}
