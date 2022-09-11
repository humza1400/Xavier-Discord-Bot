package me.comu.exeter.commands.image;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SetRGBImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Utility.beingProcessed) {
            event.getChannel().sendMessage("An image is already being processed, please wait.").queue();
            return;
        }
        if (args.size() != 3) {
            event.getChannel().sendMessage("Please specify a valid RGB, RGB value, and image to recolor the image with").queue();
            return;
        }
        if (!args.get(0).equalsIgnoreCase("R") && !args.get(0).equalsIgnoreCase("G") && !args.get(0).equalsIgnoreCase("B")) {
            event.getChannel().sendMessage("Invalid RGB type entered, try again").queue();
            return;
        }
        try {
            Integer.parseInt(args.get(1));
        } catch (IllegalArgumentException ex) {
            event.getChannel().sendMessage("Please insert a valid RGB value (0-255)").queue();
            return;
        }

        String method;
        int r = 0;
        int g = 0;
        int b = 0;
        if (args.get(0).equalsIgnoreCase("R")) {
            method = "r";
            r = Integer.parseInt(args.get(1));
        } else if (args.get(0).equalsIgnoreCase("G")) {
            method = "g";
            g = Integer.parseInt(args.get(1));
        } else if (args.get(0).equalsIgnoreCase("B")) {
            method = "b";
            b = Integer.parseInt(args.get(1));
        } else {
            event.getChannel().sendMessage("Couldn't resolve a valid RGB type").queue();
            return;
        }

        Utility.beingProcessed = true;
        if (event.getMessage().getAttachments().isEmpty()) {
            try {
                int random = new Random().nextInt(1000);
                int newRandom = new Random().nextInt(1000);
                Utility.saveImage(args.get(2), "cache", "image" + random);
                File file = new File("cache/image" + random + ".png");
                BufferedImage image = ImageIO.read(file);
                for (int i = 0; i < image.getWidth(); i++) {
                    switch (method) {
                        case "r":
                            for (int j = 0; j < image.getHeight(); j++) {
                                image.setRGB(i, j, new Color(r, 0, 0, 255).getRGB());
                            }
                            break;
                        case "g":
                            for (int j = 0; j < image.getHeight(); j++) {
                                image.setRGB(i, j, new Color(0, g, 0, 255).getRGB());
                            }
                            break;
                        case "b":
                            for (int j = 0; j < image.getHeight(); j++) {
                                image.setRGB(i, j, new Color(0, 0, b, 255).getRGB());
                            }
                            break;
                    }
                }
                File newFilePNG = new File("cache/image" + newRandom + ".png");
                ImageIO.write(image, "png", newFilePNG);
                event.getChannel().sendFile(newFilePNG, "swag.png").queue(lol -> Config.clearCacheDirectory());
                Utility.beingProcessed = false;
            } catch (Exception ex) {
                event.getChannel().sendMessage("Something went wrong with processing the image").queue();
                Utility.beingProcessed = false;
            }

        } else {
            try {
                Utility.beingProcessed = true;
                int random = new Random().nextInt(1000);
                int newRandom = new Random().nextInt(1000);
                Utility.saveImage(args.get(3), "cache", "image" + random);
                File file = new File("cache/image" + random + ".png");
                BufferedImage image = ImageIO.read(file);
                for (int i = 0; i < image.getWidth(); i++) {
                    for (int j = 0; j < image.getHeight(); j++) {
                        image.setRGB(i, j, new Color(r, g, b, 255).getRGB());
                    }
                }
                File newFilePNG = new File("cache/image" + newRandom + ".png");
                ImageIO.write(image, "png", newFilePNG);
                event.getChannel().sendFile(newFilePNG, "swag.png").queue(lol -> Config.clearCacheDirectory());
                Utility.beingProcessed = false;
            } catch (Exception ex) {
                event.getChannel().sendMessage("Something went wrong with processing the image").queue();
                Utility.beingProcessed = false;
            }
        }
        Config.clearCacheDirectory();
    }


    @Override
    public String getHelp() {
        return "Returns the specified image with only the RGB color mentioned\n`" + Core.PREFIX + getInvoke() + " [R]/[G]/[B] <value> (image)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setrgb";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setrgbimage", "rgbimage", "setrgbimg", "rgbimg"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
