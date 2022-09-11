package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
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
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Currently owner-only.").build()).queue();
            return;
        }
        if (bound) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Chat-log channel already bound. Nullifying...").build()).queue();
        }
        if (bound && !args.isEmpty() && args.get(0).equalsIgnoreCase("null")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Unbound the current logs channel: `" + channelName + "`").build()).queue();
            return;
        }
        TextChannel channel = event.getChannel();
        logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessageEmbeds(Utility.embed("Log channel bound to `#" + channelName + "`").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
