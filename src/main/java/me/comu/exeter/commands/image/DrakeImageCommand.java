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

public class DrakeImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev/drake?top={text-1}&bottom={text-2}";
        if (args.size() != 2) {
            event.getChannel().sendMessage("Please specify two words to interpolate into a Drake meme template").queue();
            return;
        }
        try {

            BufferedImage img = ImageIO.read(new URL(api.replace("{text-1}", args.get(0)).replace("{text-2}", args.get(1))));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessage("Something went wrong when interpolating the image.").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Interpolates the specified text into a Drake meme template\n`" + Core.PREFIX + getInvoke() + " [word-1] <word-2>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "drake";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"hotlinebling"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
