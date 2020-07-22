package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.Range;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GetRGBCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() != 3) {
            event.getChannel().sendMessage("Please specify an RGB color.").queue();
            return;
        }
        int r;
        int g;
        int b;
        try {
            r = Integer.parseInt(args.get(0));
            g = Integer.parseInt(args.get(1));
            b = Integer.parseInt(args.get(2));
        } catch (IllegalArgumentException ex) {
            event.getChannel().sendMessage("Please insert valid integers").queue();
            return;
        }
        Range<Integer> range = Range.between(0, 255);
        if (!(range.contains(r) && range.contains(g) && range.contains(b)))
        {
            event.getChannel().sendMessage("RGB values must be between 0-255 (inclusive)").queue();
            return;
        }
        try {
            int random = new Random().nextInt(1000);
            int newRandom = new Random().nextInt(1000);
            Utility.saveImage("https://data.whicdn.com/images/335463006/original.jpg", "cache", "image" + random);
            File file = new File("cache/image" + random + ".png");
            BufferedImage image = ImageIO.read(file);
            for(int width=0; width < image.getWidth(); width++)
            {
                for(int height=0; height < image.getHeight(); height++)
                {
                    Color temp = new Color(r, g, b);
                    image.setRGB(width, height, temp.getRGB());
                }
            }

            File newFilePNG = new File("cache/image" + newRandom + ".png");
            ImageIO.write(image, "png", newFilePNG);
            event.getChannel().sendFile(newFilePNG, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ignored) {
            event.getChannel().sendMessage("Something went wrong with processing the RGB").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Returns an image of the RGB color specified\n`" + Core.PREFIX + getInvoke() + "[r] <g> (b)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "getrgb";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"rgb", "checkrgb", "rgbcolor", "getrgbcolor", "checkrgbcolor", "colorrgb"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
