package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BindLogChannelCommand implements ICommand {

    public static boolean bound = false;
    public static long logChannelID;
    static String channelName;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("Currently owner-only.").queue();
            return;
        }
        if (bound) {
            event.getChannel().sendMessage("Chat-log channel already bound. Nullifying...").queue();
        }
        if (bound && !args.isEmpty() && args.get(0).equalsIgnoreCase("null")) {
            event.getChannel().sendMessage("Unbound the current logs channel: `" + channelName + "`").queue();
            return;
        }
        TextChannel channel = event.getChannel();
        logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessage("Log channel bound to `#" + channelName + "`").queue();
        bound = true;
    }


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
        return new String[]{"setlogs"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
