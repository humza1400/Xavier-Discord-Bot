package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EmbedMessageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.endsWith("color:red")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:red", "")).setColor(Color.RED).build()).queue();return;
        }
        if (message.endsWith("color:green")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:green", "")).setColor(Color.GREEN).build()).queue();return;
        }
        if (message.endsWith("color:blue")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:blue", "")).setColor(Color.BLUE).build()).queue();return;
        }
        if (message.endsWith("color:orange")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:orange", "")).setColor(Color.ORANGE).build()).queue();return;
        }
        if (message.endsWith("color:pink")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:pink", "")).setColor(Color.PINK).build()).queue();return;
        }
        if (message.endsWith("color:purple")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:purple", "")).setColor(0x6a0dad).build()).queue();return;
        }
        if (message.endsWith("color:black")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:black", "")).setColor(Color.BLACK).build()).queue();return;
        }
        if (message.endsWith("color:cyan")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:cyan", "")).setColor(Color.CYAN).build()).queue();return;
        }
        if (message.endsWith("color:yellow")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:yellow", "")).setColor(Color.YELLOW).build()).queue();return;
        }
        if (message.endsWith("color:white")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:white", "")).setColor(Color.WHITE).build()).queue();return;
        }
        if (message.endsWith("color:magenta")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:magenta", "")).setColor(Color.MAGENTA).build()).queue();return;
        }
        if (message.endsWith("color:gray")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:gray", "")).setColor(Color.GRAY).build()).queue();return;
        }
        if (message.endsWith("color:dark_gray")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:dark_gray", "")).setColor(Color.DARK_GRAY).build()).queue();return;
        }
        if (message.endsWith("color:light_gray")) {
            event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:light_gray", "")).setColor(Color.LIGHT_GRAY).build()).queue();return;
        }
        if (message.contains("color:")) {
            String color = message.substring(message.indexOf("color:")).replace("color:","");
            String hexColor = "0x" + color;
            try {
                event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:", "").replace(color, "")).setColor(Integer.decode(hexColor)).build()).queue();
            } catch (NumberFormatException ex)
            {
                event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "").replace("color:", "").replace(color, "")).build()).queue();
            }
            return;
        }
        event.getChannel().sendMessage(EmbedUtils.embedMessage(message.replaceFirst(Core.PREFIX + getInvoke(), "")).build()).queue();return;
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
}
