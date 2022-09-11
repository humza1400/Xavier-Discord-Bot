package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class StatusRoleCommand implements ICommand {

    public static List<String> members = new ArrayList<>();
    public static List<String> message = new ArrayList<>();
    public static String guildId = null;
    public static String roleId = null;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set the status role").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(getHelp()).build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("setrole") || args.get(0).equalsIgnoreCase("role")) {
            if (args.size() < 2) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a role").build()).queue();
                return;
            }
            try {
                Role role = event.getGuild().getRoleById(args.get(1));
                if (role == null) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find that role").build()).queue();
                    return;
                }
                if (role.hasPermission(Permission.BAN_MEMBERS) || role.hasPermission(Permission.KICK_MEMBERS) || role.hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot set a role with malicious permissions as the status role.").build()).queue();
                    return;
                }
                roleId = role.getId();
                guildId = event.getGuild().getId();
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully set to the status-role to: `" + role.getName() + "`.").build()).queue();
            } catch (NumberFormatException numberFormatException) {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.stream().skip(1).forEach(stringJoiner::add);
                List<Role> role = event.getGuild().getRolesByName(stringJoiner.toString(), true);
                if (role.isEmpty()) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find that role, maybe try using the id").build()).queue();
                } else {
                    if (role.get(0).hasPermission(Permission.BAN_MEMBERS) || role.get(0).hasPermission(Permission.KICK_MEMBERS) || role.get(0).hasPermission(Permission.ADMINISTRATOR)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot set a role with malicious permissions as the status role.").build()).queue();
                        return;
                    }
                    roleId = role.get(0).getId();
                    guildId = event.getGuild().getId();
                    event.getChannel().sendMessageEmbeds(Utility.embed("Successfully set the status-role to `" + role.get(0).getName() + "`").build()).queue();
                }
                return;
            }
        }
        if (args.get(0).equalsIgnoreCase("clearmessages") || args.get(0).equalsIgnoreCase("clearmsgs") || args.get(0).equalsIgnoreCase("clearmsg")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Cleared out **" + message.size() + "** messages.").build()).queue();
            message.clear();
            return;
        }
        if (args.get(0).equalsIgnoreCase("addmsg") || args.get(0).equalsIgnoreCase("addmessage") || args.get(0).equalsIgnoreCase("addmsgs") || args.get(0).equalsIgnoreCase("addmessages") || args.get(0).equalsIgnoreCase("msg") || args.get(0).equalsIgnoreCase("message") || args.get(0).equalsIgnoreCase("setmsg") || args.get(0).equalsIgnoreCase("setmessage")) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            message.add(Utility.removeMentions(stringJoiner.toString()));
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully added message.").build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "If a user has the specified text in their status it'll auto set their role\n`" + Core.PREFIX + getInvoke() + " [setrole]/[clearmsgs]/[addmsg] <message>`\nCurrent Messages: `" + message + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "vanityrole";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"rolestatus", "statrole", "srole", "statusroles","vanity","statusrole"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
