package me.comu.exeter.events;

import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class EditEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
        if (event.getMessage().getContentRaw().startsWith(Core.PREFIX))
        {
            event.getChannel().sendMessage(Core.DEBUG + "attempting to invoke on edit").queue();
        }

    }
}
