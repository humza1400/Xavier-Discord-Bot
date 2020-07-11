package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SetMuteRoleCommand implements ICommand {

    static final HashMap<String, String> rolesMap = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if ((!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_ROLES)) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to set the mute role").queue();
            return;
        }
        if ((!selfMember.hasPermission(Permission.MANAGE_ROLES))) {
            channel.sendMessage("I don't have permissions to set the mute role").queue();
            return;
        }

        if (args.size() != 1) {
            channel.sendMessage("Please specify a role").queue();
            return;
        }

        try {
            verifyMuteRole(event.getGuild());
            Role role = event.getGuild().getRoleById(Long.parseLong(args.get(0)));
            rolesMap.put(event.getGuild().getId(), Objects.requireNonNull(role).getId());
            channel.sendMessage("Mute role successfully set to `" + Objects.requireNonNull(role).getName() + "`").queue();
        } catch (NullPointerException | NumberFormatException ex) {
            List<Role> roles = event.getGuild().getRolesByName(args.get(0), true);
            if (roles.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find role `" + Utility.removeMentions(args.get(0)) + "`. Maybe try using the role ID instead.").queue();
                return;
            }
            if (roles.size() > 1) {
                event.getChannel().sendMessage("Multiple roles found for `" + Utility.removeMentions(args.get(0)) + "`. Use the role ID instead.").queue();
                return;
            }
            verifyMuteRole(event.getGuild());
            Role role = roles.get(0);
            rolesMap.put(event.getGuild().getId(), Objects.requireNonNull(role).getId());
            channel.sendMessage("Mute role successfully set to `" + role.getName() + "`").queue();
        }

    }

    static boolean isMuteRoleSet(Guild guild) {

        return rolesMap.containsKey(guild.getId());

    }

    public static HashMap<String, String> getMutedRoleMap() {

        return rolesMap;
    }

    private static void verifyMuteRole(Guild guild) {
        if (isMuteRoleSet(guild))
            getMutedRoleMap().remove(guild.getId());
    }

    @Override
    public String getHelp() {
        return "Sets the mute role to the specified role\n`" + Core.PREFIX + getInvoke() + " [role] <-id>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "muterole";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setmuterole", "muterole", "rolemute", "mutedrole"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
