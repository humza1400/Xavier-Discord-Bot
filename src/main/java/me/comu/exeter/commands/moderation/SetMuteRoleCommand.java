package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SetMuteRoleCommand implements ICommand {

    private static boolean isMuteRoleSet;
    private static Role role;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER) && (!member.hasPermission(Permission.MANAGE_ROLES))) {
            channel.sendMessage("You don't have permission to set the mute role").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) && (!selfMember.hasPermission(Permission.MANAGE_ROLES))) {
            channel.sendMessage("I don't have permissions to set the mute role").queue();
            return;
        }

        if (args.isEmpty() || args.size() > 1) {
            channel.sendMessage("Please specify a role").queue();
            return;
        }
        if (!args.get(0).matches("^[-0-9]+")) {
            channel.sendMessage("Please insert a valid role id").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a role");
            return;
        }
        try {
            role = event.getGuild().getRoleById(Long.parseLong(args.get(0)));
            isMuteRoleSet = true;
            channel.sendMessage("Mute role successfully set to `" + role.getName() + "`").queue();
        } catch (NullPointerException ex) {
            channel.sendMessage("That role doesn't exist").queue();
        }

    }

    public static boolean isMuteRoleSet() {
        return isMuteRoleSet;
    }

    public static Role getMutedRole() {
        return role;
    }

    @Override
    public String getHelp() {
        return "Sets the mute role to the specified role\n`" + Core.PREFIX + getInvoke() + " [role]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "muterole";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setmuterole", "muterole", "rolemute"};
    }
}
