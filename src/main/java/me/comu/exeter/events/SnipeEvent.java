package me.comu.exeter.events;

import me.comu.exeter.commands.bot.EditSnipeCommand;
import me.comu.exeter.commands.bot.SnipeCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SnipeEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
        {
            return;
        }
        if (SnipeCommand.messages.size() > 200) {
            SnipeCommand.messages.clear();
        }
        if (event.getMessage().getAttachments().isEmpty()) {
            SnipeCommand.containedAttachments = false;
            SnipeCommand.messages.put(event.getMessageId(), event.getMessage().getContentRaw());
        } else {
            SnipeCommand.containedAttachments = true;
            SnipeCommand.messages.put(event.getMessageId(), event.getMessage().getContentRaw() + "\n" + event.getMessage().getAttachments().get(0).getProxyUrl());
        }
        SnipeCommand.authors.put(event.getMessageId(), event.getMessage().getAuthor().getId());

        if (EditSnipeCommand.messages.size() > 200) {
            EditSnipeCommand.messages.clear();
        }
        if (event.getMessage().getAttachments().isEmpty()) {
            EditSnipeCommand.containedAttachments = false;
            EditSnipeCommand.messages.put(event.getMessageId(), event.getMessage().getContentRaw());
        } else {
            EditSnipeCommand.containedAttachments = true;
            EditSnipeCommand.messages.put(event.getMessageId(), event.getMessage().getContentRaw() + "\n" + event.getMessage().getAttachments().get(0).getProxyUrl());
        }
        EditSnipeCommand.authors.put(event.getMessageId(), event.getMessage().getAuthor().getId());
    }

    @Override
    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {
        if (SnipeCommand.messages.containsKey(event.getMessageId())) {
            SnipeCommand.snipeable = true;
            SnipeCommand.timeDeleted = Instant.now();
            SnipeCommand.author = SnipeCommand.authors.get(event.getMessageId());
            SnipeCommand.contentDeleted = SnipeCommand.messages.get(event.getMessageId());
        }
    }

    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
        if (EditSnipeCommand.messages.containsKey(event.getMessageId())) {
            EditSnipeCommand.snipeable = true;
            EditSnipeCommand.timeDeleted = Instant.now();
            EditSnipeCommand.author = EditSnipeCommand.authors.get(event.getMessageId());
            EditSnipeCommand.preContentDeleted = EditSnipeCommand.messages.get(event.getMessageId());
            EditSnipeCommand.postContentDeleted = event.getMessage().getContentRaw();
        }
    }
}
