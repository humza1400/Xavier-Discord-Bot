package me.comu.exeter.events;

import me.comu.exeter.commands.bot.SnipeCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SnipeEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event)
    {
        if (SnipeCommand.messages.size() > 200)
        {
            SnipeCommand.messages.clear();
        }
        if (event.getMessage().getAttachments().isEmpty()) {
            SnipeCommand.containedAttachments = false;
            SnipeCommand.messages.put(event.getMessageId(), event.getMessage().getContentRaw());
        }
        else {
            SnipeCommand.containedAttachments = true;
            SnipeCommand.messages.put(event.getMessageId(), event.getMessage().getContentRaw() + "\n" + event.getMessage().getAttachments().get(0).getUrl());
        }
        SnipeCommand.authors.put(event.getMessageId(), event.getMessage().getAuthor().getId());
    }

    @Override
    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {
        if (SnipeCommand.messages.containsKey(event.getMessageId()))
        {
        SnipeCommand.snipeable = true;
        SnipeCommand.timeDeleted = Instant.now();
        SnipeCommand.author = SnipeCommand.authors.get(event.getMessageId());
        SnipeCommand.contentDeleted = SnipeCommand.messages.get(event.getMessageId());
        }

    }

}
