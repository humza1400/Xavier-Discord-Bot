package me.comu.exeter.commands.moderation;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class BindLogChannelCommand implements ICommand {

    private EventWaiter eventWaiter;
    public static boolean bound = false;
    public static long logChannelID;
    public static String channelName;

    public BindLogChannelCommand(EventWaiter waiter) {
        this.eventWaiter = waiter;

    }
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to set a log channel").queue();
            return;
        }
        if (bound == true) {
            event.getChannel().sendMessage("Chat-log channel already bound. Nullifying...").queue();
        }
        TextChannel channel = event.getChannel();
        this.logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessage("Log channel bound to `#" + channelName + "`").queue();
        bound = true;
        if (bound) {
            eventWaiter.waitForEvent(MessageReceivedEvent.class, (e) -> e.isFromType(ChannelType.TEXT), e-> {
                channel.sendMessage((String.format("(%s)[%s]<%#s>: %s", event.getGuild().getName(), channelName, event.getAuthor(), e.getMessage()))).queue();
        });
//        eventWaiter.waitForEvent(LogMessageReceivedEvent.class, (e) ->
//                {   if (e.isFromType(ChannelType.TEXT)) {
//                        return true;
//                    }
//                    return false;
//                }, (e) -> { logger.info(String.format("[DEBUG] (%s)[%s]<%#s>: %s", event.getGuild().getName(), channelName, event.getAuthor(), e.getMessage()));}
//
//                );
        }
    }

/*    private void registerWaiter(long messageId, long channelId, ShardManager shardManager) {

    }*/

    @Override
    public String getHelp() {
        return "Binds the current text-channel to store all chat-logs\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "bindlogs";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setlogs"};
    }
}
