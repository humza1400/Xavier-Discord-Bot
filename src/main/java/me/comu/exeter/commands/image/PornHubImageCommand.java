package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class PornHubImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() != 2) {
            event.getChannel().sendMessage("Please specify two words to interpolate into a PornHub logo").queue();
            return;
        }
        final String api = "https://api.alexflipnote.dev/pornhub?text={text-1}&text2={text-2}";
        try {

            BufferedImage img = ImageIO.read(new URL(api.replace("{text-1}", args.get(0)).replace("{text-2}",args.get(1))));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessage("Something went wrong when interpolating the image.").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Interpolates the specified text into a PornHub Logo\n`" + Core.PREFIX + getInvoke() + " [word-1] <word-2>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "pornhub";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"pornhublogo", "phlogo","ph"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
