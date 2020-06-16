package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DistortImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getAttachments().isEmpty()) {
            if (args.isEmpty()) {
                event.getChannel().sendMessage("Please insert an image link to manipulate").queue();
                return;
            }
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                try {
                    int random = new Random().nextInt(1000);
                    int newRandom = new Random().nextInt(1000);
                    Wrapper.saveImage(args.get(0), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            if (i <= image.getWidth() && j <= image.getHeight()) {
                                Color c = new Color(image.getRGB(i, j));
                                int r = c.getRed();
                                int g = c.getGreen();
                                int b = c.getBlue();
                                int a = c.getAlpha();
                                Color rgb = new Color(r, g, b, a);
                                if (Math.random() > 0.6)
                                    image.setRGB(j, i, rgb.getRGB());
                                else
                                    image.setRGB(i, j, rgb.getRGB());
                            }
                        }
                    }
                    File newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(image, "png", newFilePNG);
                    message.delete().queue();
                    event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                } catch (Exception ignored) {
                    message.editMessage("Something went wrong with processing the image").queue();
                }

            });
        } else {
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                try {
                    int random = new Random().nextInt(1000);
                    int newRandom = new Random().nextInt(1000);
                    Wrapper.saveImage(args.get(0), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            Color c = new Color(image.getRGB(i, j));
                            int r = c.getRed();
                            int g = c.getGreen();
                            int b = c.getBlue();
                            int a = c.getAlpha();
                            Color rgb = new Color(r, g, b, a);
                            if (Math.random() > 0.6)
                                image.setRGB(j, i, rgb.getRGB());
                            else
                                image.setRGB(i, j, rgb.getRGB());
                        }
                    }
                    File newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(image, "png", newFilePNG);
                    message.delete().queue();
                    event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                } catch (Exception ex) {
                    message.editMessage("Something went wrong with processing the image").queue();
                }

            });
        }
        Config.clearCacheDirectory();
    }


    @Override
    public String getHelp() {
        return "Distorts the specified image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "distort";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"distortimage", "distortimg", "skew", "skewimage", "skewimg"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}