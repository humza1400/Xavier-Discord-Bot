package me.comu.exeter.commands.image;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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
import java.util.concurrent.CountDownLatch;

public class PerspectiveImageCommand implements ICommand {


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
                    File newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(distortImg(image), "png", newFilePNG);
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
                    double radius = 3.0 * image.getHeight();
                    int xC = (int) radius, yC = (int) radius;
                    BufferedImage imageB = new BufferedImage(xC * 2, yC, BufferedImage.TYPE_INT_ARGB);

                    double r, i, j;
                    for (int y = 0; y < imageB.getHeight(); y++) {
                        for (int x = 0; x < imageB.getWidth(); x++) {
                            r = Math.sqrt((xC - x) * (xC - x) + (yC - y) * (yC - y));
                            i = (radius - r);
                            j = (-3.0 * image.getWidth() / 2 * (xC - x)) / r + image.getWidth() / 2.0;
                            if (i >= 0 && i < image.getHeight() && j >= 0 && j < image.getWidth()) {
                                imageB.setRGB(x, y, image.getRGB((int) j, (int) i));
                            }
                        }
                    }

                    File newFilePNG = new File("cache/image" + newRandom + ".png");
                    ImageIO.write(imageB, "png", newFilePNG);
                    message.delete().queue();
                    event.getChannel().sendFile(newFilePNG).queue(lol -> Config.clearCacheDirectory());
                } catch (Exception ex) {
                    message.editMessage("Something went wrong with processing the image").queue();
                }

            });
        }
        Config.clearCacheDirectory();
    }

    private static BufferedImage distortImg(BufferedImage image) {
        new JFXPanel();

        final BufferedImage[] imageContainer = new BufferedImage[1];

        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int width = image.getWidth();
            int height = image.getHeight();
            Canvas canvas = new Canvas(width, height);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));

            PerspectiveTransform trans = new PerspectiveTransform();
            trans.setUlx(0);
            trans.setUly(height / 4);
            trans.setUrx(width);
            trans.setUry(0);
            trans.setLrx(width);
            trans.setLry(height);
            trans.setLlx(0);
            trans.setLly(height - height / 2);

            imageView.setEffect(trans);


            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            Image newImage = imageView.snapshot(params, null);
            graphicsContext.drawImage(newImage, 0, 0);

            imageContainer[0] = SwingFXUtils.fromFXImage(newImage, image);
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.exit();
        return imageContainer[0];
    }


    @Override
    public String getHelp() {
        return "Blurs the specified image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "perspectify";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"perspective", "perspectifyimage", "perspectifyimg", "perspectiveimage", "imageperspective", "imgperspective", "imgperspectify","imageperspectify"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}