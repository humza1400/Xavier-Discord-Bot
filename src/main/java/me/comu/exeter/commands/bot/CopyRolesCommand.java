package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.RestorableRole;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CopyRolesCommand implements ICommand {

    static List<RestorableRole> roles = new ArrayList<>();
    static boolean copied = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to copy the roles").queue();
            return;
        }
        clearCopiedRoles(CopyRolesCommand.roles);
        event.getGuild().getRoles().forEach((role -> {
            if (event.getGuild().getSelfMember().canInteract(role)) {
                if (!role.isManaged() && !role.isPublicRole()) {
                    roles.add(new RestorableRole(role));
                }

            }
        }));
        copied = true;
        event.getChannel().sendMessage("Successfully copied `" + roles.size() + "` roles.").queue();


    }

    static void clearCopiedRoles(List<RestorableRole> roles)
    {roles.clear();}


    @Override
    public String getHelp() {
        return "Copies the current guild's roles\n `" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "copyroles";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"rolecopy", "rolescopy", "copyrole", "saveroles"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
