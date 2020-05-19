package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeAllAdminCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ((event.getAuthor().getIdLong() != Core.OWNERID) && (event.getAuthor().getIdLong() != 699562509366984784L)) {
            event.getChannel().sendMessage("You don't have permission to take all admin permissions away, sorry bro").queue();
            return;
        }

        List<Role> guildRoles = event.getGuild().getRoles();
        List<String> adminRoles = new ArrayList<>();
        List<String> canAddBotRoles = new ArrayList<>();
        List<String> canBanRoles = new ArrayList<>();
        List<String> canKickRoles = new ArrayList<>();
        for (Role role : guildRoles)
        {
            if (role.hasPermission(Permission.ADMINISTRATOR) && event.getGuild().getSelfMember().canInteract(role)) {
                role.getManager().revokePermissions(Permission.ADMINISTRATOR).queue();
                adminRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.MANAGE_SERVER) && event.getGuild().getSelfMember().canInteract(role)) {
                role.getManager().revokePermissions(Permission.MANAGE_SERVER).queue();
                canAddBotRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.BAN_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                role.getManager().revokePermissions(Permission.BAN_MEMBERS).queue();
                canBanRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.KICK_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                role.getManager().revokePermissions(Permission.KICK_MEMBERS).queue();
                canKickRoles.add(role.getName());
            }
        }
        event.getChannel().sendMessage("`Admin Roles Affected:`\n" + Arrays.deepToString(adminRoles.toArray()) + "\n`Bot_Add Roles Affected:`\n" + Arrays.deepToString(canAddBotRoles.toArray()) + "\n`Ban Roles Affected:`\n" + Arrays.deepToString(canBanRoles.toArray()) + "\n`Kick Roles Affected:`\n" + Arrays.deepToString(canKickRoles.toArray())).queue();

    }

    @Override
    public String getHelp() {
        return "Takes all admin, ban, kick, bot_add permissions away from all roles under the bot\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "takealladminperms";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"takeadminperms","takealladmin"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
