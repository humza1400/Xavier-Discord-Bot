package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class MembersRoleCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a role").queue();
            return;
        }
        if (!event.getMessage().getMentionedRoles().isEmpty()) {
            Role role = event.getMessage().getMentionedRoles().get(0);
            List<Member> members = event.getGuild().getMembersWithRoles(role);
            if (members.isEmpty()) {
                event.getChannel().sendMessage("There are no members in **" + role.getName() + "**").queue();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (Member member : members) stringBuilder.append(member.getAsMention()).append("\n");
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Utility.getAmbientColor()).setTitle(members.size() + " Members in " + role.getName()).setDescription(stringBuilder.toString()).build()).queue();
            }
        } else {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String roleName = stringJoiner.toString();
            if (event.getGuild().getRolesByName(roleName, true).isEmpty()) {
                event.getChannel().sendMessage("No role found with that name, maybe try using the role-id or mentioning it instead?").queue();
            } else {
                Role role = event.getGuild().getRolesByName(roleName, true).get(0);
                List<Member> members = event.getGuild().getMembersWithRoles(role);
                if (members.isEmpty())
                    event.getChannel().sendMessage("There are no members in **" + role.getName() + "**").queue();
                else {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Member member : members) stringBuilder.append(member.getAsMention()).append("\n");
                    event.getChannel().sendMessage(new EmbedBuilder().setColor(Utility.getAmbientColor()).setTitle(members.size() + " Members in " + role.getName()).setDescription(stringBuilder.toString()).build()).queue();
                }
            }
        }
    }

    @Override
    public String getHelp() {
        return "Shows all members in the specified role\n`" + Core.PREFIX + getInvoke() + " [role]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rolemembers";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"whoisin", "whoisinrole", "rolemembers", "rolemember", "members"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
