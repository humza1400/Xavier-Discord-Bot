package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdminRolesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to list admin roles").queue();
            return;
        }


        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permissions to list admin roles").queue();
            return;
        }


        List<Role> guildRoles = event.getGuild().getRoles();
        List<String> adminRoles = new ArrayList<>();
        List<String> canAddBotRoles = new ArrayList<>();
        List<String> canBanRoles = new ArrayList<>();
        List<String> canKickRoles = new ArrayList<>();
        for (Role role : guildRoles)
        {
            if (role.hasPermission(Permission.ADMINISTRATOR)) {
                adminRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.MANAGE_SERVER) && !role.hasPermission(Permission.ADMINISTRATOR))
            {
                canAddBotRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.BAN_MEMBERS) && !role.hasPermission(Permission.ADMINISTRATOR)) {
                canBanRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.KICK_MEMBERS) && !role.hasPermission(Permission.ADMINISTRATOR)) {
                canKickRoles.add(role.getName());
            }
        }
        StringBuilder buffer = new StringBuilder("`All Roles With Administrator Permissions: (" + adminRoles.size() + ")`\n");
        if (!adminRoles.isEmpty()) {
               buffer.append(adminRoles).append("\n");
        } else {
            buffer.append("No Roles With **Administrator** Permission\n");
        }
        buffer.append("`All Roles With BOT_ADD Permissions: (").append(canAddBotRoles.size()).append(")`\n");
        if (!canAddBotRoles.isEmpty()) {
            buffer.append(canAddBotRoles).append("\n");
        } else {
            buffer.append("No Roles With **ADD_BOT** Permission\n");
        }
        buffer.append("`All Roles With BAN Permissions: (").append(canBanRoles.size()).append(")`\n");
        if (!canBanRoles.isEmpty()) {
            buffer.append(canBanRoles).append("\n");
        } else {
            buffer.append("No Roles With **BAN** Permission\n");
        }
        buffer.append("`All Roles With KICK Permissions: (").append(canKickRoles.size()).append(")`\n");
        if (!canKickRoles.isEmpty()) {
           buffer.append(canKickRoles).append("\n");
        } else {
            buffer.append("No Roles With **KICK** Permission\n");
        }
        event.getChannel().sendMessage(buffer.toString()).queue();
    }

    @Override
    public String getHelp() {

        return "Lists all roles that have admin\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "'";
    }

    @Override
    public String getInvoke() {
        return "adminroles";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"roleadmin","rolesadmin","adminrole"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
