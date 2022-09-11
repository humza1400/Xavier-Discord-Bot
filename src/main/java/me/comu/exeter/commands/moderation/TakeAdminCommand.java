package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TakeAdminCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to take their admin permissions away, sorry bro").build()).queue();
            return;
        }
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user").build()).queue();
            return;
        }
        List<Role> memberRoles = Objects.requireNonNull(event.getGuild().getMember(event.getMessage().getMentionedMembers().get(0).getUser())).getRoles();
        List<String> adminRoles = new ArrayList<>();
        List<String> canAddBotRoles = new ArrayList<>();
        List<String> canBanRoles = new ArrayList<>();
        List<String> canKickRoles = new ArrayList<>();
        for (Role role : memberRoles) {
            if (role.hasPermission(Permission.ADMINISTRATOR) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId())), role).queue();
                adminRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.MANAGE_SERVER) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId())), role).queue();
                canAddBotRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.BAN_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId())), role).queue();
                canBanRoles.add(role.getName());
            }
            if (role.hasPermission(Permission.KICK_MEMBERS) && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getGuild().getMemberById(event.getMessage().getMentionedMembers().get(0).getId())), role).queue();
                canKickRoles.add(role.getName());
            }
        }
        event.getChannel().sendMessageEmbeds(Utility.embed("`Admin Roles Affected:`\n" +
                Arrays.deepToString(adminRoles.toArray()) + "\n\n`Bot_Add Roles Affected:`\n" +
                Arrays.deepToString(canAddBotRoles.toArray()) + "\n\n`Ban Roles Affected:`\n" +
                Arrays.deepToString(canBanRoles.toArray()) + "\n\n`Kick Roles Affected:`\n" +
                Arrays.deepToString(canKickRoles.toArray())).build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
