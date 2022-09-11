package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class FilterCommand implements ICommand {

    public static final HashMap<String, String> filteredUsers = new HashMap<>();
    public static final HashMap<String, String> filteredRoles = new HashMap<>();
    private static boolean active = true;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(getHelp()).build()).queue();
            return;
        }
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle the filter").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("listroles")) {
            if (filteredRoles.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.embed("No roles are whitelisted to the filter.").build()).queue();
                return;
            }
            event.getChannel().sendMessage(filteredRoles.keySet().toString()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("listusers")) {
            if (filteredUsers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Nobody is whitelisted to the filter.").build()).queue();
                return;
            }
            event.getChannel().sendMessage(filteredUsers.keySet().toString()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clearusers") || args.get(0).equalsIgnoreCase("clearuser")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleared **" + filteredUsers.size() + "** users.").build()).queue();
            filteredUsers.clear();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clearroles") || args.get(0).equalsIgnoreCase("clearrole") || args.get(0).equalsIgnoreCase("roleclear") || args.get(0).equalsIgnoreCase("rolesclear")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleared **" + filteredRoles.size() + "** roles.").build()).queue();
            filteredRoles.clear();
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (args.get(0).equalsIgnoreCase("add") || args.get(0).equalsIgnoreCase("adduser") || args.get(0).equalsIgnoreCase("user") || args.get(0).equalsIgnoreCase("whitelist") || args.get(0).equalsIgnoreCase("wl")) {
            if (mentionedMembers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify who you want to whitelist from the filter").build()).queue();
                return;
            }
            if (filteredUsers.containsKey(mentionedMembers.get(0).getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(mentionedMembers.get(0).getAsMention() + " is already filter-whitelisted!").build()).queue();
                return;
            }
            filteredUsers.put(mentionedMembers.get(0).getId(), event.getGuild().getId());
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Added `%#s` to the filter whitelist", mentionedMembers.get(0).getUser())).build()).queue();

        } else if (args.get(0).equalsIgnoreCase("remove") || args.get(0).equalsIgnoreCase("rem") || args.get(0).equalsIgnoreCase("unwhitelist") || args.get(0).equalsIgnoreCase("uwl") || args.get(0).equalsIgnoreCase("removeuser") || args.get(0).equalsIgnoreCase("deluser")) {
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify who you want to remove from the filter whitelist").build()).queue();
                return;
            }
            if (filteredUsers.containsKey(mentionedMembers.get(0).getId())) {
                filteredUsers.remove(mentionedMembers.get(0).getId());
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Removed `%#s` from the filter whitelist", mentionedMembers.get(0).getUser())).build()).queue();
            }

        } else if (args.get(0).equalsIgnoreCase("role") || args.get(0).equalsIgnoreCase("addrole") || args.get(0).equalsIgnoreCase("whitelistrole") || args.get(0).equalsIgnoreCase("wlrole")) {
            Role role;
            try {
                if (args.size() == 1) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a role").build()).queue();
                    return;
                }
                role = event.getGuild().getRoleById(Long.parseLong(args.get(1)));
                if (filteredRoles.containsKey(Objects.requireNonNull(role).getId())) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + role.getName() + "` is already filter-whitelisted.").build()).queue();
                    return;
                }
                filteredRoles.put(Objects.requireNonNull(role).getId(), event.getGuild().getId());
                event.getChannel().sendMessageEmbeds(Utility.embed("All users with the `" + role.getName() + "` role will now be excluded from the filter.").build()).queue();
            } catch (NullPointerException | NumberFormatException ex) {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.stream().skip(1).forEach(stringJoiner::add);
                List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString().toLowerCase().replaceFirst("addrole", ""), false);
                if (roles.isEmpty()) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find role `" + Utility.removeMentions(args.get(1)) + "`. Maybe try using the role ID instead.").build()).queue();
                    return;
                }
                if (roles.size() > 1) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Multiple roles found for `" + Utility.removeMentions(args.get(1)) + "`. Use the role ID instead.").build()).queue();
                    return;
                }
                role = roles.get(0);
                if (FilterCommand.filteredRoles.containsKey(role.getId())) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + role.getName() + "` is already filter-whitelisted.").build()).queue();
                    return;
                }
                filteredRoles.put(role.getId(), event.getGuild().getId());
                event.getChannel().sendMessageEmbeds(Utility.embed("All users with the `" + role.getName() + "` role will now be excluded from the filter.").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("removerole") || args.get(0).equalsIgnoreCase("remrole") || args.get(0).equalsIgnoreCase("unwhitelistrole") || args.get(0).equalsIgnoreCase("uwlrole") || args.get(0).equalsIgnoreCase("delrole")) {
            Role role;
            try {
                if (args.size() == 1) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a role").build()).queue();
                    return;
                }
                role = event.getGuild().getRoleById(Long.parseLong(args.get(1)));
                if (filteredRoles.containsKey(Objects.requireNonNull(role).getId())) {
                    filteredRoles.remove(role.getId());
                    event.getChannel().sendMessageEmbeds(Utility.embed("Removed the `" + role.getName() + "` role from the filter-whitelist.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + role.getName() + "` is not filter-whitelisted").build()).queue();
                }
            } catch (NullPointerException | NumberFormatException ex) {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.stream().skip(1).forEach(stringJoiner::add);
                List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString().toLowerCase().replaceFirst("addrole", ""), false);
                if (roles.isEmpty()) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find role `" + Utility.removeMentions(args.get(1)) + "`. Maybe try using the role ID instead.").build()).queue();
                    return;
                }
                if (roles.size() > 1) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Multiple roles found for `" + args.get(1) + "`. Use the role ID instead.").build()).queue();
                    return;
                }
                role = roles.get(0);
                if (filteredRoles.containsKey(role.getId())) {
                    filteredRoles.remove(role.getId());
                    event.getChannel().sendMessageEmbeds(Utility.embed("Removed the `" + role.getName() + "` role from the filter-whitelist.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + role.getName() + "` is not filter-whitelisted").build()).queue();
                }
            }
        } else if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                event.getChannel().sendMessageEmbeds(Utility.embed("Filter is now **active**").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Filter is already enabled").build()).queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Filter is **no longer active**").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Filter is already disabled").build()).queue();
        }
    }

    public static boolean isActive() {
        return active;
    }

    @Override
    public String getHelp() {
        return "Toggles a text-channel filter\n`" + Core.PREFIX + getInvoke() + " [on/off] : [add/remove] <user> : [addrole/removerole] <role>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "filter";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"togglefilter"};
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
