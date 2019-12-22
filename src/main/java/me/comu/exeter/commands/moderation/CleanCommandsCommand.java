package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanCommandsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Message> deletedMessages = new ArrayList<>();
        event.getChannel().getHistory().retrievePast(100).queue((messages -> {
            for (Message message : messages)
            {
                if (message.getContentRaw().startsWith(Core.PREFIX) || message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
                {
                    deletedMessages.add(message);
                }
            }
            event.getChannel().purgeMessages(deletedMessages);
            event.getChannel().sendMessage("Successfully cleaned up **" + deletedMessages.size() + "** messages.").queue();
            event.getChannel().getHistory().retrievePast(100).queue((cleanMessages) -> {
                for (Message message : cleanMessages)
                {
                    if (message.getContentRaw().startsWith(Core.PREFIX) || message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
                    {
                        deletedMessages.add(message);
                    }
                }
                event.getChannel().purgeMessages(deletedMessages);
            });
        }));
    }

    @Override
    public String getHelp() {
        return "Cleans ALL bot messages\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clean";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"cclean","cleanmessages","cleanmsgs"};
    }
}
