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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ResizeImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Wrapper.beingProcessed) {
            event.getChannel().sendMessage("An image is already being processed, please wait.").queue();
            return;
        }
        if (args.size() < 2) {
            event.getChannel().sendMessage("Please specify a valid width and height to resize the image").queue();
            return;
        }
        int width;
        int height;
        try {
            width = Integer.parseInt(args.get(0));
            height = Integer.parseInt(args.get(1));
        } catch (IllegalArgumentException ex) {
            event.getChannel().sendMessage("Invalid dimensions were specified, please try again.").queue();
            return;
        }

        if (event.getMessage().getAttachments().isEmpty()) {
            Wrapper.beingProcessed = true;
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                try {
                    int random = new Random().nextInt(1000);
                    int newRandom = new Random().nextInt(1000);
                    Wrapper.saveImage(args.get(2), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
                    CompletableFuture.supplyAsync(() -> image)
                            .thenApply(img -> resizeImage(img, width, height))
                            .completeOnTimeout(null, 10, TimeUnit.SECONDS)
                            .thenAccept(processedImage -> {
                                if (processedImage == null) {
                                    message.editMessage("Processing thread timed out.").queue();
                                    Config.clearCacheDirectory();
                                    Wrapper.beingProcessed = false;
                                } else {
                                    try {
                                        File newFilePNG = new File("cache/image" + newRandom + ".png");
                                        ImageIO.write(processedImage, "png", newFilePNG);
                                        message.delete().queue();
                                        event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                                        Wrapper.beingProcessed = false;
                                    } catch (Exception ignored) {
                                        message.editMessage("Something went wrong with processing the image").queue();
                                        Wrapper.beingProcessed = false;
                                    }
                                }
                            });
                } catch (Exception ex) {
                    message.editMessage("Something went wrong with processing the image").queue();
                }
            });
        } else {
            Wrapper.beingProcessed = true;
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                try {
                    int random = new Random().nextInt(1000);
                    int newRandom = new Random().nextInt(1000);
                    Wrapper.saveImage(event.getMessage().getAttachments().get(0).getUrl(), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
                    CompletableFuture.supplyAsync(() -> image)
                            .thenApply(img -> resizeImage(img, width, height))
                            .completeOnTimeout(null, 10, TimeUnit.SECONDS)
                            .thenAccept(processedImage -> {
                                if (processedImage == null) {
                                    message.editMessage("Processing thread timed out.").queue();
                                    Config.clearCacheDirectory();
                                    Wrapper.beingProcessed = false;
                                } else {
                                    try {
                                        File newFilePNG = new File("cache/image" + newRandom + ".png");
                                        ImageIO.write(processedImage, "png", newFilePNG);
                                        message.delete().queue();
                                        event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                                        Wrapper.beingProcessed = false;
                                    } catch (Exception ignored) {
                                        message.editMessage("Something went wrong with processing the image").queue();
                                        Wrapper.beingProcessed = false;
                                    }
                                }
                            });
                } catch (Exception ex) {
                    message.editMessage("Something went wrong with processing the image").queue();
                    Wrapper.beingProcessed = false;
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
        return "Scales an image to the specified dimensions\n`" + Core.PREFIX + getInvoke() + " [width] <height>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "resize";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"resizeimage", "resizeimg", "scaleimage", "scaleimg", "scale"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
