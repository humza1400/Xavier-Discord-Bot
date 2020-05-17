package me.comu.exeter.commands.moderation;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BindLogChannelCommand implements ICommand {

    private final EventWaiter eventWaiter;
    public static boolean bound = false;
    public static long logChannelID;
    public static String channelName;

    public BindLogChannelCommand(EventWaiter waiter) {
        this.eventWaiter = waiter;

    }
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ( Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("Currently owner-only.").queue();
            return;
        }
        if (bound) {
            event.getChannel().sendMessage("Chat-log channel already bound. Nullifying...").queue();
        }
        if (bound && !args.isEmpty() && args.get(0).equalsIgnoreCase("null"))
        {
            event.getChannel().sendMessage("Unbound the current logs channel: `" + channelName + "`").queue();
            return;
        }
        TextChannel channel = event.getChannel();
        logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessage("Log channel bound to `#" + channelName + "`").queue();
        bound = true;
/*        if (bound) {
            eventWaiter.waitForEvent(MessageReceivedEvent.class, (e) -> e.isFromType(ChannelType.TEXT), e-> Objects.requireNonNull(event.getJDA().getTextChannelById("710368870845775913")).sendMessage((String.format("(%s)[%s]<%#s>: %s", event.getGuild().getName(), channelName, event.getAuthor(), e.getMessage()))).queue());
//        eventWaiter.waitForEvent(LogMessageReceivedEvent.class, (e) ->
//                {   if (e.isFromType(ChannelType.TEXT)) {
//                        return true;
//                    }
//                    return false;
//                }, (e) -> { logger.info(String.format("[DEBUG] (%s)[%s]<%#s>: %s", event.getGuild().getName(), channelName, event.getAuthor(), e.getMessage()));}
//
//                );
        }*/
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

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
