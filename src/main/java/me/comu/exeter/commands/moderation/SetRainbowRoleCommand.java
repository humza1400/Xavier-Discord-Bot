package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.events.RainbowRoleEvent;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetRainbowRoleCommand implements ICommand {

    private static boolean isRainbowRoleSet;
    private static Role role;
    private static long roleID;
    public static Guild guild;


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        guild = event.getGuild();


        if (/*!member.hasPermission(Permission.MANAGE_SERVER) && (!member.hasPermission(Permission.MANAGE_ROLES)) && */Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
//            channel.sendMessage("You don't have permission to set the rainbow role").queue();
            event.getChannel().sendMessage("Currently only the owner can set the rainbow role due to ratelimit handling.").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) && (!selfMember.hasPermission(Permission.MANAGE_ROLES))) {
            channel.sendMessage("I don't have permissions to set the rainbow role").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a role");
            return;
        }

        if (args.size() > 1) {
            channel.sendMessage("Please specify a role").queue();
            return;
        }
  /*      if (!args.get(0).matches("^[-0-9]+")) {
            channel.sendMessage("Please insert a valid role id").queue();
            return;
        }*/

        try {
            role = event.getGuild().getRoleById(Long.parseLong(args.get(0)));
            roleID = Objects.requireNonNull(event.getGuild().getRoleById(Long.parseLong(args.get(0)))).getIdLong();
            isRainbowRoleSet = true;
            channel.sendMessage("Rainbow role successfully set to `" + role.getName() + "`").queue();
        } catch (NullPointerException | NumberFormatException ex) {
                List<Role> roles = event.getGuild().getRolesByName(args.get(0), true);
                if (roles.isEmpty())
                {
                    event.getChannel().sendMessage("Couldn't find role `" + args.get(0) + "`. Maybe try using the role ID instead.".replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                    return;
                }
                if (roles.size() > 1)
                {
                    event.getChannel().sendMessage("Multiple roles found for `" + args.get(0) + "`. Use the role ID instead.").queue();
                    return;
                }
                role = roles.get(0);
                isRainbowRoleSet = true;
                channel.sendMessage("Rainbow role successfully set to `" + role.getName() + "`").queue();
        }

    }

    public static boolean isIsRainbowRoleSet() {
        return isRainbowRoleSet;
    }

    public static long getRoleID() {
        return roleID;
    }

    public static Role getRainbowRole() {
        return role;
    }

    public static void nullifyRainbowRole()
    {
        role = null;
        isRainbowRoleSet = false;
        Core.jda.removeEventListener(new RainbowRoleEvent());
    }
    @Override
    public String getHelp() {
        return "Sets the rainbow role to the specified role\n`" + Core.PREFIX + getInvoke() + " [role]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rainbow";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setrainbowrole", "rainbowrole","setrainbow","setuprainbow", "rolerainbow"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
