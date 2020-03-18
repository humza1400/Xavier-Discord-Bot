package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class GiveRoleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to modify that user").queue();
            return;
        }


        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permissions to modify that user").queue();
            return;
        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase("role") || event.getMessage().getContentRaw().equalsIgnoreCase("giverole"))
        {
            event.getChannel().sendMessage("Please specify a valid user and valid role").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("all"))
        {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString(), true);
            if (roles.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the role " + stringJoiner.toString()).queue();
                return;
            } else if (roles.size() > 1) {
                event.getChannel().sendMessage("Multiple roles found! Try using the role ID instead.").queue();
                return;
            }
            for (Member m : event.getGuild().getMembers())
            {
                if (!m.getRoles().contains(roles.get(0)))
                {
                    event.getGuild().addRoleToMember(m, roles.get(0)).reason("Given by " + event.getAuthor().getAsTag()).queue();
                }
            }
            event.getChannel().sendMessage("Giving everyone the `" + roles.get(0).getName() + "` role, this may take some time").queue();
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (mentionedMembers.isEmpty()) {
            List<Member> members = event.getGuild().getMembersByName(args.get(0), true);
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString(), true);
            if (members.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (members.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            if (roles.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the role " + stringJoiner.toString()).queue();
                return;
            } else if (roles.size() > 1) {
                event.getChannel().sendMessage("Multiple roles found! Try using the role ID instead.").queue();
                return;
            }
            if (!event.getMember().canInteract(roles.get(0)))
            {
                event.getChannel().sendMessage("You cannot modify a role or user whilst it is a higher precedent than your own").queue();
                return;
            }
            if (members.get(0).getRoles().contains(roles.get(0)))
            {  try {
                event.getGuild().removeRoleFromMember(members.get(0), roles.get(0)).queue();
                event.getChannel().sendMessage("Removed **" + roles.get(0).getName() + "** from **" + members.get(0).getAsMention() + "**.").queue();
                return;
            } catch (HierarchyException ex)
            {
                event.getChannel().sendMessage("I cannot modify a role or user whilst it is a higher precedent than my own").queue();
                return;
            }
            }
            try {
                event.getGuild().addRoleToMember(members.get(0), roles.get(0)).reason("Given by " + event.getAuthor().getAsTag()).queue();
                event.getChannel().sendMessage("Added **" + roles.get(0).getName() + "** to **" + members.get(0).getAsMention() + "**.").queue();
            } catch (HierarchyException ex)
            {
                event.getChannel().sendMessage("I cannot modify a role or user whilst it is a higher precedent than my own").queue();
            }
            return;
        }
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString(), true);
            if (roles.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the role " + stringJoiner.toString()).queue();
                return;
            } else if (roles.size() > 1) {
                event.getChannel().sendMessage("Multiple roles found! Try using the role ID instead.").queue();
                return;
            }
        if (!event.getMember().canInteract(roles.get(0)))
        {
            event.getChannel().sendMessage("You cannot modify a role or user whilst it is a higher precedent than your own").queue();
            return;
        }
        if (mentionedMembers.get(0).getRoles().contains(roles.get(0))) {
            try {
                event.getGuild().removeRoleFromMember(mentionedMembers.get(0), roles.get(0)).queue();
                event.getChannel().sendMessage("Removed **" + roles.get(0).getName() + "** from **" + mentionedMembers.get(0).getAsMention() + "**.").queue();
                return;
            } catch (HierarchyException ex) {
                event.getChannel().sendMessage("I cannot modify a role or user whilst it is a higher precedent than my own").queue();
                return;
            }
        }
            try {
                event.getGuild().addRoleToMember(mentionedMembers.get(0), roles.get(0)).reason("Given by " + event.getAuthor().getAsTag()).queue();
                event.getChannel().sendMessage("Added **" + roles.get(0).getName() + "** to **" + mentionedMembers.get(0).getAsMention() + "**.").queue();
            } catch (HierarchyException ex)
            {
                event.getChannel().sendMessage("I cannot modify a role or user whilst it is a higher precedent than my own").queue();
            }

    }

    @Override
    public String getHelp() {
        return "Gives the specified role to the specified role\n" + "`"  + Core.PREFIX + getInvoke() + " [user] <role>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "role";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"giverole"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
