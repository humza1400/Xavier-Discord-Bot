package me.comu.exeter.events;

import me.comu.exeter.commands.bot.ReactionRoleCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReactionRoleEvent extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            if (event.getMessageId().equalsIgnoreCase(ReactionRoleCommand.messageID) && event.getReactionEmote().getEmote().getId().equalsIgnoreCase(ReactionRoleCommand.emoji.getId())) {
                Role role = event.getGuild().getRoleById(ReactionRoleCommand.roleID);
                if (event.getMember() != null && role != null)
                    event.getGuild().addRoleToMember(event.getMember(), role).queue(null, failure -> event.getChannel().sendMessage("The reaction role is at a higher precedent than my own, so I can't assign it.").queue());

            }
        } else {
            if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE) && event.getMessageId().equalsIgnoreCase(ReactionRoleCommand.messageID) && event.getReactionEmote().getEmote().getId().equalsIgnoreCase(ReactionRoleCommand.emoji.getId()))
                event.getChannel().sendMessage("Can't assign roles because I'm lacking permissions").queue(null, null);
        }
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        if (event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            if (event.getMessageId().equalsIgnoreCase(ReactionRoleCommand.messageID) && event.getReactionEmote().getEmote().getId().equalsIgnoreCase(ReactionRoleCommand.emoji.getId())) {
                Role role = event.getGuild().getRoleById(ReactionRoleCommand.roleID);
                if (event.getMember() != null && role != null)
                    event.getGuild().removeRoleFromMember(event.getMember(), role).queue(null, failure -> event.getChannel().sendMessage("The reaction role is at a higher precedent than my own, so I can't assign it.").queue());
            }
        } else {
            if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE) && event.getMessageId().equalsIgnoreCase(ReactionRoleCommand.messageID) && event.getReactionEmote().getEmote().getId().equalsIgnoreCase(ReactionRoleCommand.emoji.getId()))
                event.getChannel().sendMessage("Can't assign roles because I'm lacking permissions").queue(null, null);
        }
    }
}
