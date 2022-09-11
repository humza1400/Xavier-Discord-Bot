package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class GiveRoleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to manage roles").build()).queue();
            return;
        }


        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to modify that user").build()).queue();
            return;
        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase(Core.PREFIX + "role") || event.getMessage().getContentRaw().equalsIgnoreCase(Core.PREFIX + "giverole")) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a valid user and valid role").build()).queue();
            return;
        }
        if (args.size() < 2) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a valid user and a valid role").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("all")) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString(), true);
            if (roles.isEmpty()) {
                try {
                    Role role = Objects.requireNonNull(event.getGuild().getRoleById(args.get(1)));
                    if (!event.getGuild().getSelfMember().canInteract(role)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permission to interact with that role!").build()).queue();
                        return;
                    }
                    if (event.getMember().canInteract(role)) {
                        for (Member m : event.getGuild().getMembers()) {
                            if (!m.getRoles().contains(role)) {
                                event.getGuild().addRoleToMember(m, role).reason("Given by " + event.getAuthor().getAsTag()).queue();
                            }
                        }
                        event.getChannel().sendMessageEmbeds(Utility.embed("Giving everyone the `" + role.getName() + "` role, this may take some time").build()).queue();
                        return;
                    } else {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to interact with that role!").build()).queue();
                        return;
                    }
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find a role matching that ID").build()).queue();
                } catch (NumberFormatException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the role " + Utility.removeMentions(stringJoiner.toString())).build()).queue();
                    return;
                }
            }
            if (event.getMember().canInteract(roles.get(0))) {
                for (Member m : event.getGuild().getMembers()) {
                    if (!m.getRoles().contains(roles.get(0))) {
                        event.getGuild().addRoleToMember(m, roles.get(0)).reason("Given by " + event.getAuthor().getAsTag()).queue();
                    }
                }
                event.getChannel().sendMessageEmbeds(Utility.embed("Giving everyone the `" + roles.get(0).getName() + "` role, this may take some time").build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to interact with that role!").build()).queue();
            }
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (mentionedMembers.isEmpty()) {
            List<Member> members = event.getGuild().getMembersByName(args.get(0), true);
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString(), true);
            if (members.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            }
            if (roles.isEmpty()) {
                try {
                    Role role = Objects.requireNonNull(event.getGuild().getRoleById(args.get(1)));
                    if (!event.getMember().canInteract(role)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot modify a role or user whilst it is a higher precedent than your own").build()).queue();
                        return;
                    }
                    if (!event.getGuild().getSelfMember().canInteract(role)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot modify a role or user whilst it is a higher precedent than my own").build()).queue();
                        return;
                    }
                    if (members.get(0).getRoles().contains(role)) {
                        event.getGuild().removeRoleFromMember(members.get(0), role).queue();
                        event.getChannel().sendMessageEmbeds(Utility.embed("Removed **" + role.getName() + "** from **" + members.get(0).getAsMention() + "**.").build()).queue();
                    } else {
                        event.getGuild().addRoleToMember(members.get(0), role).reason("Given by " + event.getAuthor().getAsTag()).queue();
                        event.getChannel().sendMessageEmbeds(Utility.embed("Added **" + role.getName() + "** to **" + members.get(0).getAsMention() + "**.").build()).queue();
                    }
                    return;
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find a role matching that ID").build()).queue();
                } catch (NumberFormatException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the role " + Utility.removeMentions(stringJoiner.toString())).build()).queue();
                    return;
                }
            }
            if (!event.getMember().canInteract(roles.get(0))) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot modify a role or user whilst it is a higher precedent than your own").build()).queue();
                return;
            }
            if (members.get(0).getRoles().contains(roles.get(0))) {
                try {
                    event.getGuild().removeRoleFromMember(members.get(0), roles.get(0)).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed("Removed **" + roles.get(0).getName() + "** from **" + members.get(0).getAsMention() + "**.").build()).queue();
                    return;
                } catch (HierarchyException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot modify a role or user whilst it is a higher precedent than my own").build()).queue();
                    return;
                }
            }
            try {
                event.getGuild().addRoleToMember(members.get(0), roles.get(0)).reason("Given by " + event.getAuthor().getAsTag()).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed("Added **" + roles.get(0).getName() + "** to **" + members.get(0).getAsMention() + "**.").build()).queue();
            } catch (HierarchyException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot modify a role or user whilst it is a higher precedent than my own").build()).queue();
            }
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        List<Role> roles = event.getGuild().getRolesByName(stringJoiner.toString(), true);
        if (roles.isEmpty()) {
            try {
                Role role = Objects.requireNonNull(event.getGuild().getRoleById(args.get(1)));
                if (!event.getMember().canInteract(role)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot modify a role or user whilst it is a higher precedent than your own").build()).queue();
                    return;
                }
                if (!event.getGuild().getSelfMember().canInteract(role)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot modify a role or user whilst it is a higher precedent than my own").build()).queue();
                    return;
                }
                if (mentionedMembers.get(0).getRoles().contains(role)) {
                    event.getGuild().removeRoleFromMember(mentionedMembers.get(0), role).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed("Removed **" + role.getName() + "** from **" + mentionedMembers.get(0).getAsMention() + "**.").build()).queue();
                } else {
                    event.getGuild().addRoleToMember(mentionedMembers.get(0), role).reason("Given by " + event.getAuthor().getAsTag()).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed("Added **" + role.getName() + "** to **" + mentionedMembers.get(0).getAsMention() + "**.").build()).queue();
                }
                return;

            } catch (NullPointerException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find a role matching that ID").build()).queue();
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the role " + Utility.removeMentions(stringJoiner.toString())).build()).queue();
                return;
            }
        }
        if (!event.getMember().canInteract(roles.get(0))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot modify a role or user whilst it is a higher precedent than your own").build()).queue();
            return;
        }
        if (mentionedMembers.get(0).getRoles().contains(roles.get(0))) {
            try {
                event.getGuild().removeRoleFromMember(mentionedMembers.get(0), roles.get(0)).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed("Removed **" + roles.get(0).getName() + "** from **" + mentionedMembers.get(0).getAsMention() + "**.").build()).queue();
                return;
            } catch (HierarchyException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot modify a role or user whilst it is a higher precedent than my own").build()).queue();
                return;
            }
        }
        try {
            event.getGuild().addRoleToMember(mentionedMembers.get(0), roles.get(0)).reason("Given by " + event.getAuthor().getAsTag()).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Added **" + roles.get(0).getName() + "** to **" + mentionedMembers.get(0).getAsMention() + "**.").build()).queue();
        } catch (HierarchyException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot modify a role or user whilst it is a higher precedent than my own").build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Gives the specified role to the specified user\n" + "`" + Core.PREFIX + getInvoke() + " [user] <role>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "role";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"giverole"};
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