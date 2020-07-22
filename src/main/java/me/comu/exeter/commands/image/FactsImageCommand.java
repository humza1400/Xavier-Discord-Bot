package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class FactsImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev/facts?text=";
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a message you would like to interpolate into a facts meme").queue();
            return;
        }
        try {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String message = stringJoiner.toString().replaceAll(" ", "%20");
            BufferedImage img = ImageIO.read(new URL(api + message));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex)
        {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessage("The message content cannot be over 2000 characters!").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Makes a facts meme template with the text provided\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias())+ "`";
    }

    @Override
    public String getInvoke() {
        return "facts";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"factify"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
