package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetConfessionChannelCommand implements ICommand {

    public static boolean bound = false;
    public static long logChannelID;
    private static String channelName;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to set the Confession Channel").queue();
            return;
        }
        if (bound && !args.isEmpty() && (args.get(0).equalsIgnoreCase("null") || args.get(0).equalsIgnoreCase("nullify")))
        {
            event.getChannel().sendMessage("Unbound the current confession channel: `" + channelName + "`").queue();
            bound = false;
            return;
        } else if (bound) {
            event.getChannel().sendMessage("Confession channel already bound. Nullifying...").queue();
        }
        TextChannel channel = event.getChannel();
        logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessage(EmbedUtils.embedMessage("Please DM the bot `" + Core.PREFIX + "confess [confession]` to give your anonymous confession to the server!").setTitle("Confession Channel Set To #" + channelName).setColor(Color.BLUE).setFooter("Confessed by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();
        bound = true;
    }

    @Override
    public String getHelp() {
        return "Binds the current text-channel to send confession messages\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setconfession";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setconfessionchannel","setconfessionschannel","setconfessions"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
