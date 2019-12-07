package me.comu.exeter.events;

import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class GuildJoinEvent extends ListenerAdapter {

    org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);

    @Override
    public void onGuildJoin(@Nonnull net.dv8tion.jda.api.events.guild.GuildJoinEvent event) {
        logger.info("Joined " + event.getGuild().getName() + " (" + event.getGuild().getOwner().getUser().getName() + "#" + event.getGuild().getOwner().getUser().getDiscriminator() + ")");
    }
}
