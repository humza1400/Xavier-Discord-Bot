package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.FilterCommand;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class FilterEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (FilterCommand.isActive()) {
            if (event.getMember() == null) {
                return;
            }
            if (event.getMember().getIdLong() != Core.OWNERID && !event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    String message = event.getMessage().getContentRaw();
                    if (message.contains(".gg/")) {
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention()).queue();
                    }
                }

            }
        }
    }

    @Override
    public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
        if (FilterCommand.isActive()) {
            if (event.getMember() == null) {
                return;
            }
            if (event.getMember().getIdLong() != Core.OWNERID && !event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
                if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    String message = event.getMessage().getContentRaw();
                    if (message.contains(".gg/")) {
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Invite links are not allowed, " + event.getMember().getAsMention()).queue();
                    }
                }

            }
        }
    }
}
