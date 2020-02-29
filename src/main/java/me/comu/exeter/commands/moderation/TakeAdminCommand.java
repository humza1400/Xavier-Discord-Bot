package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakeAdminCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ((event.getAuthor().getIdLong() != Core.OWNERID) && (event.getAuthor().getIdLong() != 664551103190401026L)) {
            event.getChannel().sendMessage("You don't have permission to take their admin permissions away, sorry bro").queue();
            return;
        }
        if (event.getMessage().getMentionedMembers().isEmpty())
        {
            event.getChannel().sendMessage("Please specify a user").queue();
        }
        List<Role> memberRoles = event.getGuild().getMember(event.getMessage().getMentionedMembers().get(0).getUser()).getRoles();
        List<String> adminRoles = new ArrayList<>();
        List<String> canAddBotRoles = new ArrayList<>();
        List<String> canBanRoles = new ArrayList<>();
        List<String> canKickRoles = new ArrayList<>();
        for (Role role : memberRoles)
        {
            if (role.hasPermission(Permission.ADMINISTRATOR) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId()), role).queue();
                adminRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.MANAGE_SERVER) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId()), role).queue();
                canAddBotRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.BAN_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId()), role).queue();
                canBanRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.KICK_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId()), role).queue();
                canKickRoles.add(role.getName());
            }
        }
        event.getChannel().sendMessage("`Admin Roles Affected:`\n" + Arrays.deepToString(adminRoles.toArray()) + "\n`Bot_Add Roles Affected:`\n" + Arrays.deepToString(canAddBotRoles.toArray()) + "\n`Ban Roles Affected:`\n" + Arrays.deepToString(canBanRoles.toArray()) + "\n`Kick Roles Affected:`\n" + Arrays.deepToString(canKickRoles.toArray())).queue();

    }

    @Override
    public String getHelp() {
        return "Takes all admin, ban, kick, bot_add permissions away from the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "takeadmin";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
