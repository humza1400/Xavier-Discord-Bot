package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.AutoPurgeImagesCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AIPEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (AutoPurgeImagesCommand.aipChannels.contains(event.getChannel().getId()))
        {
            if (!Objects.requireNonNull(event.getMessage().getMember()).hasPermission(Permission.ADMINISTRATOR) && !event.getMessage().getAttachments().isEmpty())
            {
                event.getMessage().delete().reason("Auto Image Purge").queueAfter(5, TimeUnit.MINUTES);
            }
        }
    }
}
