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

public class CallingMemeImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev//calling?text=";
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify words to interpolate into a meme-call template").queue();
            return;
        }
        try {

            BufferedImage img = ImageIO.read(new URL(api + String.join(" ", args).replaceAll(" ", "%20")));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            ex.printStackTrace();
            Config.clearCacheDirectory();
            event.getChannel().sendMessage("Something went wrong when interpolating the image.").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Interpolates the specified text into a Tom Calling meme template\n`" + Core.PREFIX + getInvoke() + " [message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "tomcalling";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"callingmeme", "calling"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
