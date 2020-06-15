package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GrayScaleImageCommand implements ICommand {


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
                    int width = image.getWidth();
                    int height = image.getHeight();
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int p = image.getRGB(x, y);
                            int a = (p >> 24) & 0xff;
                            int r = (p >> 16) & 0xff;
                            int g = (p >> 8) & 0xff;
                            int b = p & 0xff;
                            int avg = (r + g + b) / 3;
                            p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                            image.setRGB(x, y, p);
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
                    Wrapper.saveImage(event.getMessage().getAttachments().get(0).getUrl(), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
                    int width = image.getWidth();
                    int height = image.getHeight();
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int p = image.getRGB(x, y);
                            int a = (p >> 24) & 0xff;
                            int r = (p >> 16) & 0xff;
                            int g = (p >> 8) & 0xff;
                            int b = p & 0xff;
                            int avg = (r + g + b) / 3;
                            p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                            image.setRGB(x, y, p);
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
        return "Makes the picture black and white\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "grayscale";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"blackandwhite", "blackwhite", "blacknwhite", "grayscaleimage", "grayscaleimg"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}