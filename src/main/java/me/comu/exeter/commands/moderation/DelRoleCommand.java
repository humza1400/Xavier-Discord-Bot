package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DelRoleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to delete roles").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to delete roles").build()).queue();
            return;
        }

        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please enter a role name to delete").build()).queue();
            return;
        }
        try {
         Role role = event.getGuild().getRoleById(Long.parseLong(args.get(0)));
            if (role == null) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No role exists with that id, maybe try using the role-name").build()).queue();
                return;
            }
            if (!event.getGuild().getSelfMember().canInteract(role)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have sufficient permissions to delete that role").build()).queue();
                return;
            }
            if (!event.getMember().canInteract(role)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have sufficient permissions to delete that role").build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Successfully deleted `%s`", role.getName())).build()).queue();
            role.delete().queue();
        } catch (NullPointerException | NumberFormatException ex) {
            String roleName = String.join(" ", args);
            List<Role> rolesByName = event.getGuild().getRolesByName(roleName, true);
            if (rolesByName.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I couldn't find that role, try using the role-id instead.").build()).queue();
                return;
            }
            if (rolesByName.size() > 1) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Multiple roles found, try using the role-id instead.").build()).queue();
                return;
            }
            if (!event.getGuild().getSelfMember().canInteract(rolesByName.get(0))) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have sufficient permissions to delete that role").build()).queue();
                return;
            }
            if (!event.getMember().canInteract(rolesByName.get(0))) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have sufficient permissions to delete that role").build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Successfully deleted `%s`", rolesByName.get(0).getName())).build()).queue();
            rolesByName.get(0).delete().queue();
        }
    }

    @Override
    public String getHelp() {
        return "Creates a role\n `" + Core.PREFIX + getInvoke() + "` [name]\nAliases:`" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "delrole";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"dr","deleterole"};
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
