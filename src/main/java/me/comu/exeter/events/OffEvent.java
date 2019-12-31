package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.OffCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class OffEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (OffCommand.shouldDelete) {
            if (event.getAuthor().getId().equals(OffCommand.userID)) {
                event.getMessage().delete().queue();
            }
        }
    }
}
