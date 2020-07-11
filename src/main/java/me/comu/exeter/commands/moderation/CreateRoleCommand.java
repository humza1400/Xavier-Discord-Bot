package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class CreateRoleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to delete roles").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permissions to delete roles").queue();
            return;
        }

    if (args.isEmpty())
    {
        event.getChannel().sendMessage("Please speicfy a role to delete").queue();
        return;
    }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String roleName = stringJoiner.toString();
        event.getGuild().createRole().setName(roleName).queue();
        event.getChannel().sendMessage(String.format("Successfully created `%s`", roleName)).queue();
    }

    @Override
    public String getHelp() {
        return "Creates a role\n `" + Core.PREFIX + getInvoke() + "` [name]\nAliases:`" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "createrole";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"addrole","cr"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
