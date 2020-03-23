package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class MembersRoleCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to view role members").queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a role").queue();
            return;
        }
        if (!event.getMessage().getMentionedRoles().isEmpty())
        {
            Role role = event.getMessage().getMentionedRoles().get(0);
            List<String> members = new ArrayList<>();
            for (Member member : event.getGuild().getMembers())
            {
                for (Role memberRole : member.getRoles())
                {
                    if (memberRole.getId().equalsIgnoreCase(role.getId()))
                    {
                        members.add(member.getAsMention());
                    }
                }
            }
            if (members.isEmpty())
            event.getChannel().sendMessage("There are no members in **" + role.getName() + "**");
             else
            event.getChannel().sendMessage("**All Members In " + role.getName() + "**:\n" + members.toString()).queue();
        } else {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String roleName = stringJoiner.toString();
            if (event.getGuild().getRolesByName(roleName, true).isEmpty())
            {
               event.getChannel().sendMessage("No role found with that name, maybe try mentioning it instead?").queue();
            } else {
                Role role = event.getGuild().getRolesByName(roleName, true).get(0);
                List<String> members = new ArrayList<>();
                for (Member member : event.getGuild().getMembers())
                {
                    for (Role memberRole : member.getRoles())
                    {
                        if (memberRole.getId().equalsIgnoreCase(role.getId()))
                        {
                            members.add(member.getAsMention());
                        }
                    }
                }
                if (members.isEmpty())
                    event.getChannel().sendMessage("There are no members in **" + role.getName() + "**");
                else
                    event.getChannel().sendMessage("**All Members In " + role.getName() + "**:\n" + members.toString()).queue();
            }
        }

    }

    @Override
    public String getHelp() {
        return "Shows all members in the specified role\n`" + Core.PREFIX + getInvoke() + " [role]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "members";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"whoisin","whoisinrole","rolemembers"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
