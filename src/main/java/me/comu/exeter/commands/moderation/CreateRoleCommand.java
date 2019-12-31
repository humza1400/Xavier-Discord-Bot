package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CreateRoleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to unban someone").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permissions to unban someone").queue();
            return;
        }

    if (args.isEmpty())
    {
        event.getChannel().sendMessage("Please enter the role name").queue();
        return;
    }
        String roleName = args.get(0).replace("_"," ");
        event.getGuild().createRole().setName(roleName).queue();
        event.getChannel().sendMessage(String.format("Successfully created `%s`", roleName)).queue();
    }

    @Override
    public String getHelp() {
        return "Creates a role\n `" + Core.PREFIX + getInvoke() + "` [name]\nAliases:`" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "cr";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"addrole","createrole"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
