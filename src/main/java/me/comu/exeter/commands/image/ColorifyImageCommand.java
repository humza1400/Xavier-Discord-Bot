package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ColorifyImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() < 2) {
            event.getChannel().sendMessage("You need to specify an optional user and two hex colors").queue();
        }
        User user;
        String hex1;
        String hex2;
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            user = event.getAuthor();
            hex1 = args.get(0).replaceAll("#", "");
            hex2 = args.get(1).replaceAll("#", "");
        } else {
            user = event.getMessage().getMentionedMembers().get(0).getUser();
            hex1 = args.get(1).replaceAll("#", "");
            hex2 = args.get(2).replaceAll("#", "");
        }
        String url = "https://api.alexflipnote.dev/colourify?image=" + user.getAvatarUrl() + "[&c=" + hex1 +"]|&b=" + hex2 + "]".replaceAll(" ", "%20");
        try {
            BufferedImage img = ImageIO.read(new URL(Utility.extractUrls(url).get(0)));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessage("The message can't be over 2,000 characters!").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Contrasts the image with the specified hex-colors\n`" + Core.PREFIX + getInvoke() + " [user] <hex-color> (hex-color)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "colorify";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}