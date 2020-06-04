package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.BlacklistWordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BlacklistedWordsEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getMember() != null && !event.getMember().hasPermission(Permission.ADMINISTRATOR) && !event.getMember().getId().equals(event.getGuild().getSelfMember().getId())) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            for (String string : args) {
                if (BlacklistWordCommand.blacklistedWords.contains(string.toLowerCase())) {
                    if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                        event.getChannel().sendMessage("A blacklisted word was sent but I don't have permission to do anything :/").queue();
                        return;
                    }
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + " sent a message that contained a blacklisted word.").queue();
                    return;
                }
            }
        }

    }
}
