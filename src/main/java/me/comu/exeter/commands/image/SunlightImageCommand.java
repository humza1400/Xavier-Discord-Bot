package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SunlightImageCommand implements ICommand {


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
                    int radius = 2;
                    int size = radius * 2 + 1;
                    float weight = 2.0f / (size * size);
                    float[] data = new float[size * size];

                    for (int i = 0; i < data.length; i++) {
                        data[i] = weight;
                    }

                    Kernel kernel = new Kernel(size, size, data);
                    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage bufferedImage = op.filter(image, null);
                    File newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(bufferedImage, "png", newFilePNG);
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
                    int radius = 2;
                    int size = radius * 2 + 1;
                    float weight = 2.0f / (size * size);
                    float[] data = new float[size * size];

                    for (int i = 0; i < data.length; i++) {
                        data[i] = weight;
                    }

                    Kernel kernel = new Kernel(size, size, data);
                    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage bufferedImage = op.filter(image, null);
                    File newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(bufferedImage, "png", newFilePNG);
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
        return "Adds a sunlight filter to the specified image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "sunlight";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"sunlightfilter", "naturallight"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}