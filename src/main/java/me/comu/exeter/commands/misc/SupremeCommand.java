package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SupremeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev/supreme?text=";
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a message you would like to interpolate into *Supreme* text").queue();
            return;
        }
        try {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String message = stringJoiner.toString().replaceAll(" ", "%20");
                event.getChannel().sendMessage(EmbedUtils.embedImage(api + message).build()).queue();
        } catch (IllegalArgumentException ex)
        {
            event.getChannel().sendMessage("The message content cannot be over 2000 characters!").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Interpolates the specified text in *Supreme* format\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias())+ "`";
    }

    @Override
    public String getInvoke() {
        return "supreme";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

     @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
