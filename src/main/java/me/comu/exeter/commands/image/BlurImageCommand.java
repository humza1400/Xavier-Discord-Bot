package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class BlurImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        {
            String url = "https://api.alexflipnote.dev/filter/blur?image=";

            if (args.isEmpty() && event.getMessage().getAttachments().isEmpty()) {
                url = url + event.getAuthor().getEffectiveAvatarUrl();
            }

            if (!event.getMessage().getMentionedMembers().isEmpty()) {
                url = url + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl().replace(".gif", ".png");
            } else if (!args.isEmpty() && !Utility.isUrl(args.get(0))) {
                event.getChannel().sendMessage("I couldn't resolve the specified URL").queue();
                return;
            }

            if (!args.isEmpty() && Utility.isUrl(args.get(0))) {
                url = url + args.get(0).replace(".gif", ".png");
            } else if (!event.getMessage().getAttachments().isEmpty()) {
                url = url + event.getMessage().getAttachments().get(0).getUrl();
            }
            try {
                BufferedImage img = ImageIO.read(new URL(url));
                File file = new File("cache/downloaded.png");
                ImageIO.write(img, "png", file);
                event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
            } catch (Exception ex) {
                Config.clearCacheDirectory();
                event.getChannel().sendMessage("Something went wrong, try again later").queue();
            }


        }
    }
/*        if (Utility.beingProcessed) {
            event.getChannel().sendMessage("An image is already being processed, please wait.").queue();
            return;
        }
        if (event.getMessage().getAttachments().isEmpty()) {
            if (args.isEmpty()) {
                event.getChannel().sendMessage("Please insert an image link to manipulate").queue();
                return;
            }
            Utility.beingProcessed = true;
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                try {
                    int random = new Random().nextInt(1000);
                    int newRandom = new Random().nextInt(1000);
                    Utility.saveImage(args.get(0), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
                          *//*              int radius = 11;
                    int size = radius * 2 + 1;
                    float weight = 1.0f / (size * size);
                    float[] data = new float[size * size];

                    for (int i = 0; i < data.length; i++) {
                        data[i] = weight;
                    }

                    Kernel kernel = new Kernel(size, size, data);
                    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage bufferedImage = op.filter(image, null);*//*
                    CompletableFuture.supplyAsync(() -> image)
                            .thenApply(this::blurImg)
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
            Utility.beingProcessed = true;
            event.getChannel().sendMessage("`Processing Image...`").queue(message -> {
                try {
                    int random = new Random().nextInt(1000);
                    int newRandom = new Random().nextInt(1000);
                    Utility.saveImage(event.getMessage().getAttachments().get(0).getUrl(), "cache", "image" + random);
                    File file = new File("cache/image" + random + ".png");
                    BufferedImage image = ImageIO.read(file);
      *//*              int radius = 11;
                    int size = radius * 2 + 1;
                    float weight = 1.0f / (size * size);
                    float[] data = new float[size * size];

                    for (int i = 0; i < data.length; i++) {
                        data[i] = weight;
                    }

                    Kernel kernel = new Kernel(size, size, data);
                    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage bufferedImage = op.filter(image, null);*//*
                    CompletableFuture.supplyAsync(() -> image)
                            .thenApply(this::blurImg)
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

    private BufferedImage blurImg(BufferedImage image) {
        new JFXPanel();

        final BufferedImage[] imageContainer = new BufferedImage[1];

        final CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int width = image.getWidth();
            int height = image.getHeight();
            Canvas canvas = new Canvas(width, height);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));

            imageView.setEffect(new GaussianBlur());


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
    }*/


    @Override
    public String getHelp() {
        return "Blurs the specified image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "blur";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"blurimage", "blurimg", "blurry"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}