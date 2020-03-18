package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.SetSuggestionChannelCommand;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class SuggestionMessageCleanerEvent extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (SetSuggestionChannelCommand.bound && event.getChannel().getIdLong() == SetSuggestionChannelCommand.logChannelID)
        {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && !event.getMessage().getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
                String[] message = event.getMessage().getContentRaw().split("\\s+");
                if (!(message[0].equalsIgnoreCase(Core.PREFIX + "suggestion") || message[0].equalsIgnoreCase(Core.PREFIX + "givesuggestion") || message[0].equalsIgnoreCase(Core.PREFIX + "recommend"))) {
                    event.getMessage().delete().reason("Suggestion Cleaner").queue();
                }
            }
        }
    }
}
