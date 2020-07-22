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

public class GayImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String url = "https://api.alexflipnote.dev/filter/gay?image=";
        if (args.isEmpty() && event.getMessage().getAttachments().isEmpty()) {
            url = url + event.getAuthor().getEffectiveAvatarUrl();
        }
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            url = url + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl().replace(".gif",".png");
        } else if (!args.isEmpty() && !Utility.isUrl(args.get(0))) {
            event.getChannel().sendMessage("I couldn't resolve the specified URL").queue();
            return;
        }

        if (!args.isEmpty() && Utility.isUrl(args.get(0))) {
            url = url + args.get(0).replace(".gif",".png");
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


    @Override
    public String getHelp() {
        return "Adds a gay filter\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "gay";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"gayimage","gayimg","gayify"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}