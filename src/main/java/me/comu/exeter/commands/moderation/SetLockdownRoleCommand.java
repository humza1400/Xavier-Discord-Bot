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
import java.util.Objects;

public class SetLockdownRoleCommand implements ICommand {

    private static boolean isLockdownRoleSet;
    private static Role role;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_CHANNEL) && (!member.hasPermission(Permission.MANAGE_CHANNEL)) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to lockdown the channel").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            channel.sendMessage("I don't have permissions to lockdown the channel").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a role").queue();
            return;
        }

  /*      if (!args.get(0).matches("^[-0-9]+")) {
            channel.sendMessage("Please insert a valid role id").queue();
            return;
        }*/


        try {
            role = event.getGuild().getRoleById(Long.parseLong(args.get(0)));
            isLockdownRoleSet = true;
            channel.sendMessage("Lockdown role successfully set to `" + Objects.requireNonNull(role).getName() + "`").queue();
        } catch (NullPointerException | NumberFormatException ex) {
            List<Role> roles = event.getGuild().getRolesByName(args.get(0), true);
            if (roles.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find role `" + args.get(0) + "`. Maybe try using the role ID instead.".replaceAll("@everyone", "everyone").replaceAll("@here", "here")).queue();
                return;
            }
            if (roles.size() > 1) {
                event.getChannel().sendMessage("Multiple roles found for `" + args.get(0) + "`. Use the role ID instead.").queue();
                return;
            }
            role = roles.get(0);
            isLockdownRoleSet = true;
            channel.sendMessage("Lockdown role successfully set to `" + role.getName() + "`").queue();
        }
    }

    public static boolean isIsLockdownRoleSet() {
        return isLockdownRoleSet;
    }

    public static Role getLockdownRole() {
        return role;
    }

    @Override
    public String getHelp() {
        return "Sets the lockdown role to the specified role (defaults to @ everyone)\n`" + Core.PREFIX + getInvoke() + " [role]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lockdownrole";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ldrole"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
