package me.comu.exeter.events;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class SnipeEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {
        
    }

}
