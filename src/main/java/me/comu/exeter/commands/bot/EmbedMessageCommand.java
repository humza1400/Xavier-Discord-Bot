package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EmbedMessageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String message = Utility.removeMentions(event.getMessage().getContentRaw());
        if (message.endsWith("color:red")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:red", "")).setColor(Color.RED).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:green")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:green", "")).setColor(Color.GREEN).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:blue")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:blue", "")).setColor(Color.BLUE).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:orange")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:orange", "")).setColor(Color.ORANGE).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:pink")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:pink", "")).setColor(Color.PINK).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:purple")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:purple", "")).setColor(0x6a0dad).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:black")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:black", "")).setColor(Color.BLACK).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:cyan")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:cyan", "")).setColor(Color.CYAN).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:yellow")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:yellow", "")).setColor(Color.YELLOW).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:white")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:white", "")).setColor(Color.WHITE).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:magenta")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:magenta", "")).setColor(Color.MAGENTA).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:gray")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:gray", "")).setColor(Color.GRAY).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:dark_gray")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:dark_gray", "")).setColor(Color.DARK_GRAY).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.endsWith("color:light_gray")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:light_gray", "")).setColor(Color.LIGHT_GRAY).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();return;
        }
        if (message.contains("color:")) {
            String color = message.substring(message.indexOf("color:")).replace("color:","");
            String hexColor = "0x" + color;
            try {
                event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:", "").replace(color, "")).setColor(Integer.decode(hexColor)).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();
            } catch (NumberFormatException ex)
            {
                event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:", "").replace(color, "")).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();
            }
            return;
        }
        event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "")).setFooter("By " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Embeds the specified message\n`" + Core.PREFIX + getInvoke() + " [message] <color:(color)>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "embed";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

     @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
