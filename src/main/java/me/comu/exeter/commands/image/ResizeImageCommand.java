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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResizeImageCommand implements ICommand {

    private boolean running = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!running) {
            running = true;
            event.getChannel().sendMessage("`Resizing image...`").queue(
                    message -> {
                        Wrapper.saveImage(args.get(0), "cache/", "image69.jpg");
                        String inputImagePath = "cache/image69.jpg";
                        String outputImagePath1 = "cache/image69_fixed.jpg";
                        String outputImagePath2 = "cache/image69_smaller.jpg";
                        String outputImagePath3 = "cache/image69_larger.jpg";

                        try {
                            int scaledWidth = 1024;
                            int scaledHeight = 768;
                            resize2(inputImagePath, outputImagePath1, scaledWidth, scaledHeight);

                            double percent = 0.5;
                            resize(inputImagePath, outputImagePath2, percent);

                            percent = 1.5;
                            resize(inputImagePath, outputImagePath3, percent);
                            event.getChannel().sendFile(new File(outputImagePath1)).queue();
                        } catch (IOException ex) {
                            event.getChannel().sendMessage("Error resizing the image.").queue();
                            ex.printStackTrace();
                        }
                        Config.clearCacheDirectory();
                        running = false;
                    }
            );
        } else {
            event.getChannel().sendMessage("An image is already being processed, please wait for that to finish.").queue();
        }


    }

    private void resize(String inputImagePath, String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize2(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }

    private void resize2(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws
            IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }


    @Override
    public String getHelp() {
        return "Returns the avatar of a designated user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "resize";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"enlarge", "reduce","proportion"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
