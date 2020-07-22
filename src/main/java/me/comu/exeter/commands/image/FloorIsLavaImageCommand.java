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

public class FloorIsLavaImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("You need to specify text").queue();
            return;
        }
        final String api = "https://api.alexflipnote.dev//floor?image=" + event.getAuthor().getEffectiveAvatarUrl() + "&text=" + String.join(" ", args).replaceAll(" ", "%20");
        try {

            BufferedImage img = ImageIO.read(new URL(api));
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
        return "Makes a floor-is-lava meme\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "floorislava";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"lava"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
