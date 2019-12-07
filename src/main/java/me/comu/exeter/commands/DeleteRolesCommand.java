package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class DeleteRolesCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Role> roles = event.getGuild().getRoles();
        int roleSize = roles.size();
            try {
                for (int i = 0; i <= roleSize ; i++) {
                    try {
                        roles.get(i).delete().complete();
                    } catch (HierarchyException ex1) {
//                        event.getChannel().sendMessage(" cannot delete roles higher than mine (skipping)").queue();
                    }
                }
            } catch(HierarchyException | ErrorResponseException | ArrayIndexOutOfBoundsException ex) {
//                event.getChannel().sendMessage(" cannot delete roles higher than mine (skipping)").queue();
            }
            List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
            event.getChannel().deleteMessages(messages).queue();
            event.getChannel().sendMessage("Deleted roles").queue();
    }

    @Override
    public String getHelp() {
        return "Deletes all the roles on the server\n `" + Core.PREFIX + getInvoke() + "`\n`" + Arrays.deepToString(getAlias())+"`";
    }

    @Override
    public String getInvoke() {
        return "delroles";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"deleteroles","rolesdelete"};
    }
    
    
}
