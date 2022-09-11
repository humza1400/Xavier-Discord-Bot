package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WhoHasAdminCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to list the admins").build()).queue();
            return;
        }


        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to list the admins").build()).queue();
            return;
        }


        List<Member> guildMembers = event.getGuild().getMembers();
        List<String> adminRoles = new ArrayList<>();
        List<String> canAddBotRoles = new ArrayList<>();
        List<String> canBanRoles = new ArrayList<>();
        List<String> canKickRoles = new ArrayList<>();
        for (Member member : guildMembers) {
            if (member.hasPermission(Permission.ADMINISTRATOR)) {
                adminRoles.add(member.getAsMention());
            }
            if (member.hasPermission(Permission.MANAGE_SERVER) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                canAddBotRoles.add(member.getAsMention());
            }
            if (member.hasPermission(Permission.BAN_MEMBERS) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                canBanRoles.add(member.getAsMention());
            }
            if (member.hasPermission(Permission.KICK_MEMBERS) && !member.hasPermission(Permission.ADMINISTRATOR)) {
                canKickRoles.add(member.getAsMention());
            }
        }
        StringBuilder buffer = new StringBuilder("`All Members With Administrator Permissions: (" + adminRoles.size() + ")`\n");
        if (!adminRoles.isEmpty()) {
            buffer.append(adminRoles).append("\n");
        } else {
            buffer.append("No Members With **Administrator** Permission\n");
        }
        buffer.append("`All Members With BOT_ADD Permissions: (").append(canAddBotRoles.size()).append(")`\n");
        if (!canAddBotRoles.isEmpty()) {
            buffer.append(canAddBotRoles).append("\n");
        } else {
            buffer.append("No Members With **BOT_ADD** Permission\n");
        }
        buffer.append("`All Members With BAN Permissions: (").append(canBanRoles.size()).append(")`\n");
        if (!canBanRoles.isEmpty()) {
            buffer.append(canBanRoles).append("\n");
        } else {
            buffer.append("No Members With **BAN** Permission\n");
        }
        buffer.append("`All Members With KICK Permissions: (").append(canKickRoles.size()).append(")`\n");
        if (!canKickRoles.isEmpty()) {
            buffer.append(canKickRoles).append("\n");
        } else {
            buffer.append("No Members With **KICK** Permission\n");
        }
        event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(buffer.toString()).setColor(Core.getInstance().getColorTheme()).setTitle("Admins").setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now()).build()).queue();
    }

    @Override
    public String getHelp() {

        return "Lists all roles that have admin\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "whoadmin";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"whohasadmin", "admins", "checkadmins"};
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
