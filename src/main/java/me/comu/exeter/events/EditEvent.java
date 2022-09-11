package me.comu.exeter.events;

import me.comu.exeter.commands.CommandManager;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class EditEvent extends ListenerAdapter {

    private final CommandManager manager;

    public EditEvent(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
        if (event.getMessage().isEdited() && event.getMessage().getContentRaw().startsWith(Core.PREFIX))
        {
            String content = event.getMessage().getContentRaw();
            String[] args = content.split("\\s+");
            String messageContent = args[0].replaceFirst(Core.PREFIX, "");
            Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            OffsetDateTime now = OffsetDateTime.ofInstant(gmt.toInstant(), gmt.getTimeZone().toZoneId());
            OffsetDateTime msgCreated = event.getMessage().getTimeCreated();
            OffsetDateTime msgEdited = event.getMessage().getTimeEdited();
            boolean firstCreated = msgCreated.isBefore(now.minusSeconds(60));
            boolean created = now.isBefore(msgCreated.plusSeconds(60));
            boolean edited = now.isBefore(Objects.requireNonNull(msgEdited).plusSeconds(60));
            if (!firstCreated && (created || edited) && manager.getCommand(messageContent) != null) {
                ICommand command = manager.getCommand(messageContent);
                command.handle(Arrays.stream(args).skip(1).collect(Collectors.toList()), new GuildMessageReceivedEvent(event.getJDA(), event.getResponseNumber(), event.getMessage()));
            }

        }
    }


}
