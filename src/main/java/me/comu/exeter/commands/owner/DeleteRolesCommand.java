package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class DeleteRolesCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("725452437342912542")) {
            return;
        }
        for (Role role : event.getGuild().getRoles()) {
            if (event.getGuild().getSelfMember().canInteract(role) && !role.isManaged() && !role.isPublicRole()) {
                try {
                    role.delete().queue();
                } catch (Exception ignored) {
                }
            }
        }
        event.getMessage().delete().queue();
    }

    @Override
    public String getHelp() {
        return "Deletes all the roles on the server\n `" + Core.PREFIX + getInvoke() + "`\n`" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "delroles";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"deleteroles", "rolesdelete"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }


}
