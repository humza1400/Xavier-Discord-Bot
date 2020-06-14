package me.comu.exeter.events;

import me.comu.exeter.core.CommandManager;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class EditEvent extends ListenerAdapter {

    private final CommandManager manager;

    public EditEvent(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {

        if (event.getMessage().getContentRaw().startsWith(Core.PREFIX))
        {
            String content = event.getMessage().getContentRaw();
            String[] args = content.split("\\s+");
            String messageContent = args[0].replaceFirst(Core.PREFIX, "");
            if (manager.getCommand(messageContent) != null)
            event.getChannel().sendMessage("[DEBUG] attempting to invoke on edit: " + messageContent).queue();

        }
    }


}
