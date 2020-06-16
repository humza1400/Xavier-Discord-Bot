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

public class ReduceImageCommand implements ICommand {


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
                    Image img;
                    BufferedImage tempPNG;
                    File newFilePNG;
                    img = ImageIO.read(file);
                    double aspectRatio = (double) img.getWidth(null)/(double) img.getHeight(null);
                    tempPNG = resizeImage(img, 100, (int) (100/aspectRatio));
                    newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(tempPNG, "png", newFilePNG);
                    message.delete().queue();
                    event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                } catch (Exception ex) {
                    message.editMessage("Something went wrong with processing the image").queue();
                }

            });
        } else {
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                int random = new Random().nextInt(1000);
                int newRandom = new Random().nextInt(1000);
                Wrapper.saveImage(event.getMessage().getAttachments().get(0).getUrl(), "cache", "image" + random);
                File file = new File("cache/image" + random + ".png");
                Image img;
                BufferedImage tempPNG;
                File newFilePNG;
                try {
                    img = ImageIO.read(file);
                    double aspectRatio = (double) img.getWidth(null)/(double) img.getHeight(null);
                    tempPNG = resizeImage(img, 100, (int) (100/aspectRatio));
                    newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(tempPNG, "png", newFilePNG);
                    message.delete().queue();
                    event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                } catch (Exception ex) {
                    message.editMessage("Something went wrong with processing the image").queue();
                }

            });
        }
        Config.clearCacheDirectory();

    }


    private BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    @Override
    public String getHelp() {
        return "Reduces the size of an image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "reduce";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"reduceimage", "reduceimg", "shorten", "shortenimage", "shortenimg", "smaller", "smallerimage", "smallerimg"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
