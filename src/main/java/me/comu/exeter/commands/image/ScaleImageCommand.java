package me.comu.exeter.commands.image;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ScaleImageCommand implements ICommand {

    private boolean running = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!running) {
            try {
                Wrapper.saveImage(args.get(0), "cache", "image.png");
                File file = new File("image.png");
                BufferedImage bufferedImage = ImageIO.read(file);
            } catch (IOException ex) {
                event.getChannel().sendMessage("Something went wrong with processing the image").queue();
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                event.getChannel().sendMessage("Please insert valid dimensions").queue();
            }
        } else {
            event.getChannel().sendMessage("An image is already being processed, please wait for that to finish.").queue();
        }
    }
    public static BufferedImage getFasterScaledInstance(BufferedImage img, int targetWidth, int targetHeight, boolean progressiveBilinear) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w, h;
        int prevW = ret.getWidth();
        int prevH = ret.getHeight();
        if (progressiveBilinear) {
            w = img.getWidth();
            h = img.getHeight();
        } else {
            w = targetWidth;
            h = targetHeight;
        }
        do {
            if (progressiveBilinear && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (progressiveBilinear && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }
        return ret;
    }
    @Override
    public String getHelp() {
        return "Scales an image to the specified dimensions\n`" + Core.PREFIX + getInvoke() + " [width] <height>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "scale";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"scaleimage", "scaleimg"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
