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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class WarpImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Utility.beingProcessed) {
            event.getChannel().sendMessage("An image is already being processed, please wait.").queue();
            return;
        }
            if (event.getMessage().getAttachments().isEmpty()) {
                if (args.isEmpty()) {
                    event.getChannel().sendMessage("Please insert an image link to manipulate").queue();
                    return;
                }
                event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                    try {
                        int random = new Random().nextInt(1000);
                        int newRandom = new Random().nextInt(1000);
                        Utility.saveImage(args.get(0), "cache", "image" + random);
                        File file = new File("cache/image" + random + ".png");
                        BufferedImage image = ImageIO.read(file);
                        CompletableFuture.supplyAsync(() -> image)
                                .thenApply(this::warpImg)
                                .completeOnTimeout(null, 10, TimeUnit.SECONDS)
                                .thenAccept(processedImage -> {
                                    if (processedImage == null) {
                                        message.editMessage("Processing thread timed out.").queue();
                                        Config.clearCacheDirectory();
                                        Utility.beingProcessed = false;
                                    } else {
                                        try {
                                            File newFilePNG = new File("cache/image" + newRandom + ".png");
                                            ImageIO.write(processedImage, "png", newFilePNG);
                                            message.delete().queue();
                                            event.getChannel().sendFile(newFilePNG, "swag.png").queue(lol -> Config.clearCacheDirectory());
                                            Utility.beingProcessed = false;
                                        } catch (Exception ignored) {
                                            message.editMessage("Something went wrong with processing the image").queue();
                                            Utility.beingProcessed = false;
                                        }
                                    }
                                });
                    } catch (Exception ignored) {
                        message.editMessage("Something went wrong with processing the image").queue();
                        Utility.beingProcessed = false;
                    }

                });
            } else {
                event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                    try {
                        int random = new Random().nextInt(1000);
                        int newRandom = new Random().nextInt(1000);
                        Utility.saveImage(args.get(0), "cache", "image" + random);
                        File file = new File("cache/image" + random + ".png");
                        BufferedImage image = ImageIO.read(file);
                        CompletableFuture.supplyAsync(() -> image)
                                .thenApply(this::warpImg)
                                .completeOnTimeout(null, 10, TimeUnit.SECONDS)
                                .thenAccept(processedImage -> {
                                    if (processedImage == null) {
                                        message.editMessage("Processing thread timed out.").queue();
                                        Config.clearCacheDirectory();
                                        Utility.beingProcessed = false;
                                    } else {
                                        try {
                                            File newFilePNG = new File("cache/image" + newRandom + ".png");
                                            ImageIO.write(processedImage, "png", newFilePNG);
                                            message.delete().queue();
                                            event.getChannel().sendFile(newFilePNG, "swag.png").queue(lol -> Config.clearCacheDirectory());
                                            Utility.beingProcessed = false;
                                        } catch (Exception ignored) {
                                            message.editMessage("Something went wrong with processing the image").queue();
                                            Utility.beingProcessed = false;
                                        }
                                    }
                                });
                    } catch (Exception ex) {
                        message.editMessage("Something went wrong with processing the image").queue();
                        Utility.beingProcessed = false;
                    }

                });
            }
        Config.clearCacheDirectory();
    }

    private BufferedImage warpImg(BufferedImage image)
    {
        if (Math.random() > 0.6) {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {

                    Color c = new Color(image.getRGB(i, j));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                    Color rgb = new Color(r, g, b, a);
                    image.setRGB(j, i, rgb.getRGB());
                }
            }
        } else {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {

                    Color c = new Color(image.getRGB(i, j));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int a = c.getAlpha();
                    Color rgb = new Color(r, g, b, a);
                    image.setRGB(i, j, rgb.getRGB());
                }
            }
        }
        return image;
    }


    @Override
    public String getHelp() {
        return "Warps the specified image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "warp";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"warpimg"};
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