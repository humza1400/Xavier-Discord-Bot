package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhoHasAdminCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to list the admins").queue();
            return;
        }


        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permissions to list the admins").queue();
            return;
        }


        List<Member> guildMembers = event.getGuild().getMembers();
        List<String> adminRoles = new ArrayList<>();
        List<String> canAddBotRoles = new ArrayList<>();
        List<String> canBanRoles = new ArrayList<>();
        List<String> canKickRoles = new ArrayList<>();
        for (Member member : guildMembers)
        {
            if (member.hasPermission(Permission.ADMINISTRATOR)) {
                adminRoles.add(member.getAsMention());
            }
            if (member.hasPermission(Permission.MANAGE_SERVER) && !member.hasPermission(Permission.ADMINISTRATOR))
            {
                canAddBotRoles.add(member.getAsMention());
            }
            if (member.hasPermission(Permission.BAN_MEMBERS) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                canBanRoles.add(member.getAsMention());
            }
            if (member.hasPermission(Permission.KICK_MEMBERS) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                canKickRoles.add(member.getAsMention());
            }
        }
        StringBuffer buffer = new StringBuffer("`All Members With Administrator Permissions: (" + adminRoles.size() + ")`\n");
        if (!adminRoles.isEmpty()) {
               buffer.append(adminRoles + "\n");
        } else {
            buffer.append("No Members With **Administrator** Permission\n");
        }
        buffer.append("`All Members With BOT_ADD Permissions: (" + canAddBotRoles.size() +")`\n");
        if (!canAddBotRoles.isEmpty()) {
            buffer.append(canAddBotRoles + "\n");
        } else {
            buffer.append("No Members With **ADD_BOT** Permission\n");
        }
        buffer.append("`All Members With BAN Permissions: (" + canBanRoles.size() + ")`\n");
        if (!canBanRoles.isEmpty()) {
            buffer.append(canBanRoles + "\n");
        } else {
            buffer.append("No Members With **BAN** Permission\n");
        }
        buffer.append("`All Members With KICK Permissions: (" + canKickRoles.size() + ")`\n");
        if (!canKickRoles.isEmpty()) {
           buffer.append(canKickRoles + "\n");
        } else {
            buffer.append("No Members With **KICK** Permission\n");
        }
        event.getChannel().sendMessage(buffer.toString()).queue();
    }

    @Override
    public String getHelp() {

        return "Lists all roles that have admin\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "'";
    }

    @Override
    public String getInvoke() {
        return "whoadmin";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"whohasadmin", "admins","checkadmins"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
