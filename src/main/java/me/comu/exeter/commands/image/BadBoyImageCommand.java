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

public class BadBoyImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getMentionedMembers().isEmpty())
        {
            event.getChannel().sendMessage("You need to specify a bad boy!").queue();
            return;
        }
        final String api = "https://api.alexflipnote.dev/bad?image=" + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl().replace(".gif",".png");

        try {
            BufferedImage img = ImageIO.read(new URL(api));
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
        return "Interpolates the specified text into a bad boy meme format\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "badboy";
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
