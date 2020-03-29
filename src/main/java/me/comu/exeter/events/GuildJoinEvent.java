package me.comu.exeter.events;

import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;

class GuildJoinEvent extends ListenerAdapter {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);

    @Override
    public void onGuildJoin(@Nonnull net.dv8tion.jda.api.events.guild.GuildJoinEvent event) {
        logger.info("Joined " + event.getGuild().getName() + " (" + Objects.requireNonNull(event.getGuild().getOwner()).getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator() + ")");
    }
}
