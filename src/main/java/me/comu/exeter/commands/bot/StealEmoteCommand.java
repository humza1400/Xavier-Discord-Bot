package me.comu.exeter.commands.bot;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StealEmoteCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_EMOTES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to modify emotes.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_EMOTES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to modify emotes.").build()).queue();
            return;
        }

        if (!event.getMessage().getAttachments().isEmpty() && args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to specify a name for the emoji if you're using an attachment.").build()).queue();
            return;
        }
/*        System.out.println(event.getGuild().getEmotes().size());
        System.out.println(event.getGuild().getMaxEmotes());
        if (event.getGuild().getEmotes().size() == event.getGuild().getMaxEmotes()) {
            event.getChannel().sendMessage("You reached the max amount of emojis in your guild!").queue();
            Config.clearCacheDirectory();
            return;
        }*/

        if (!event.getMessage().getAttachments().isEmpty() && !args.isEmpty()) {
            String link = event.getMessage().getAttachments().get(0).getUrl();
            String name = args.get(0);
            if (link.contains("gif") || link.contains("jpg") || link.contains("png") || link.contains("jpeg")) {
                if (link.contains("gif")) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Creating " + name + " emoji...").build()).queue(
                            message -> {
                                Utility.saveGif(link, "cache", args.get(1));
                                message.editMessageEmbeds(Utility.embed("\u2699 Saving emoji...").build()).queue(message1 -> {
                                    try {
                                        event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".gif"))).queue(success -> {
                                            message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<a:" + name + ":" + success.getId() + ">").build()).queue();
                                            Config.clearCacheDirectory();
                                        }, failure -> {

                                            try {
                                                File file = new File("cache/" + name + ".png");
                                                BufferedImage bufferedImage = ImageIO.read(file);
                                                ImageIO.write(resizeImage(bufferedImage), "png", file);
                                                event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success -> {
                                                    message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                                    Config.clearCacheDirectory();
                                                }, failure2 -> {
                                                    failure2.printStackTrace();
                                                    Config.clearCacheDirectory();
                                                    message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                                });
                                            } catch (IOException ex) {
                                                message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                                Config.clearCacheDirectory();
                                                ex.printStackTrace();
                                            }

                                        });
                                    } catch (Exception ex) {
                                        message.editMessageEmbeds(Utility.embed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue();
                                        ex.printStackTrace();
                                    }
                                });
                            }
                    );
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Creating " + name + " emoji...").build()).queue(
                            message -> {
                                Utility.saveImage(link, "cache", name);
                                message.editMessageEmbeds(Utility.embed("\u2699 Saving emoji...").build()).queue(message1 -> {
                                    try {
                                        event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success -> {
                                            message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                            Config.clearCacheDirectory();
                                        }, failure -> {
                                            try {
                                                File file = new File("cache/" + name + ".png");
                                                BufferedImage bufferedImage = ImageIO.read(file);
                                                ImageIO.write(resizeImage(bufferedImage), "png", file);
                                                event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success -> {
                                                    message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                                    Config.clearCacheDirectory();
                                                }, failure2 -> {
                                                    failure2.printStackTrace();
                                                    Config.clearCacheDirectory();
                                                    message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                                });
                                            } catch (IOException ex) {
                                                message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                                Config.clearCacheDirectory();
                                                ex.printStackTrace();
                                            }
                                        });
                                    } catch (Exception ex) {
                                        message.editMessageEmbeds(Utility.embed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue();
                                        ex.printStackTrace();
                                    }
                                });
                            }
                    );
                }
            } else {
                event.getChannel().sendMessageEmbeds(Utility.embed("You can only add an attachment as an emoji if it's a picture or gif!").build()).queue();
            }
            return;
        }

        if (event.getMessage().getEmotes().size() == 0 && Utility.extractUrls(event.getMessage().getContentRaw()).isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid emote or link").build()).queue();
            return;
        }
        if (event.getMessage().getEmotes().size() == 0 && Utility.extractUrls(event.getMessage().getContentRaw()).size() > 0 && args.size() == 2) {
            List<String> links = Utility.extractUrls(event.getMessage().getContentRaw()).stream().filter(link -> (link.contains("gif") || link.contains("png") || link.contains("jpg") || link.contains("jpeg"))).collect(Collectors.toList());
            if (links.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't resolve a valid link").build()).queue();
                return;
            }
            String link = links.get(0);
            String name = args.get(1);
            if (link.contains("gif")) {
                event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Creating " + name + " emoji...").build()).queue(
                        message -> {
                            Utility.saveGif(link, "cache", name);
                            message.editMessageEmbeds(Utility.embed("\u2699 Saving emoji...").build()).queue(message1 -> {
                                try {
                                    event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".gif"))).queue(success -> {
                                        message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<a:" + name + ":" + success.getId() + ">").build()).queue();
                                        Config.clearCacheDirectory();
                                    }, failure -> {
                                        try {
                                            File file = new File("cache/" + name + ".gif");
                                            BufferedImage bufferedImage = ImageIO.read(file);
                                            ImageIO.write(resizeImage(bufferedImage), "gif", file);
                                            event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".gif"))).queue(success -> {
                                                message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                                Config.clearCacheDirectory();
                                            }, failure2 -> {
                                                failure2.printStackTrace();
                                                Config.clearCacheDirectory();
                                                message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                            });
                                        } catch (Exception ex) {
                                            message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                            Config.clearCacheDirectory();
                                            ex.printStackTrace();
                                        }
                                    });
                                } catch (Exception ex) {
                                    message.editMessageEmbeds(Utility.embed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue();
                                    ex.printStackTrace();
                                }
                            });
                        }
                );
            } else {
                event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Creating " + name + " emoji...").build()).queue(
                        message -> {
                            Utility.saveImage(link, "cache", name);
                            message.editMessageEmbeds(Utility.embed("\u2699 Saving emoji...").build()).queue(message1 -> {
                                try {
                                    event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success -> {
                                        message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                        Config.clearCacheDirectory();
                                    }, failure -> {
                                        try {
                                            File file = new File("cache/" + name + ".png");
                                            BufferedImage bufferedImage = ImageIO.read(file);
                                            ImageIO.write(resizeImage(bufferedImage), "png", file);
                                            event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success -> {
                                                message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                                Config.clearCacheDirectory();
                                            }, failure2 -> {
                                                failure2.printStackTrace();
                                                Config.clearCacheDirectory();
                                                message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                            });
                                        } catch (IOException ex) {
                                            message.editMessageEmbeds(Utility.embed("Something went wrong with resizing the emoji, try again later").build()).queue();
                                            Config.clearCacheDirectory();
                                            ex.printStackTrace();
                                        }
                                    });
                                } catch (Exception ex) {
                                    message.editMessageEmbeds(Utility.embed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue();
                                    ex.printStackTrace();
                                }
                            });
                        }
                );
            }
            return;
        } else if (event.getMessage().getEmotes().size() == 0 && Utility.extractUrls(event.getMessage().getContentRaw()).size() > 0 && args.size() < 2) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("If you're trying to create an emoji from a link, you need to specify a name after the link.").build()).queue();
            return;
        }


        Emote emote = event.getMessage().getEmotes().get(0);
        String name = args.size() == 1 ? emote.getName() : args.get(1);
        if (emote.isAnimated()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Creating " + name + " emoji...").build()).queue(
                    message -> {
                        Utility.saveGif(emote.getImageUrl(), "cache", name);
                        message.editMessageEmbeds(Utility.embed("\u2699 Saving emoji...").build()).queue(message1 -> {
                            try {
                                event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".gif"))).queue(success -> {
                                    message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<a:" + name + ":" + success.getId() + ">").build()).queue();
                                    Config.clearCacheDirectory();
                                }, failure -> message.editMessageEmbeds(Utility.errorEmbed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue());
                            } catch (Exception ex) {
                                message.editMessageEmbeds(Utility.errorEmbed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue();
                                ex.printStackTrace();
                            }
                        });
                    }
            );

        } else {
            event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Creating " + name + " emoji...").build()).queue(
                    message -> {
                        Utility.saveImage(emote.getImageUrl(), "cache", name);
                        message.editMessageEmbeds(Utility.embed("\u2699 Saving emoji...").build()).queue(message1 -> {
                            try {
                                event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success -> {
                                    message1.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully created " + "<:" + name + ":" + success.getId() + ">").build()).queue();
                                    Config.clearCacheDirectory();
                                }, failure -> message.editMessageEmbeds(Utility.embed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue());
                            } catch (Exception ex) {
                                message.editMessageEmbeds(Utility.embed("<a:no:959656234108190760> Something went wrong try again later.").build()).queue();
                                ex.printStackTrace();
                            }
                        });
                    }
            );
        }
        Config.clearCacheDirectory();
    }

    private Image resizeGif(final Image image) {
        return image.getScaledInstance(128, 128, java.awt.Image.SCALE_SMOOTH);
    }

    private BufferedImage resizeImage(final Image image) {
        final BufferedImage bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, 128, 128, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    @Override
    public String getHelp() {
        return "Adds the given emoji, link, or attachment as an emote to the server\n `" + Core.PREFIX + getInvoke() + " [emote] <emote-name>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "stealemote";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"addemote", "addemoji", "stealemoji", "emoteadd", "emojiadd", "emotesteal", "emojisteal", "steal"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
