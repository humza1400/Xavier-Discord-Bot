package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AmIAJokeImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        User user;
        if (event.getMessage().getMentionedMembers().isEmpty())
        {
            user = event.getAuthor();
        } else {
            user = event.getMessage().getMentionedMembers().get(0).getUser();
        }
        String url = "https://api.alexflipnote.dev/amiajoke?image=" + user.getEffectiveAvatarUrl();
        try {
            BufferedImage img = ImageIO.read(new URL(url));
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
        return "Makes an \"Am I A Joke To You?\" meme template\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "amiajoke";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"amiajoketoyou", "amiajoketou", "aiaj"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}