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

public class AchievementImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("You need to specify a message").queue();
            return;
        }
        int random = Utility.randomNum(1, 45);
        String url = "https://api.alexflipnote.dev/achievement?text=" + String.join("%20", args) + "&icon=" + random;
        try {
            BufferedImage img = ImageIO.read(new URL(url));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex)
        {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessage("The message can't be over 2,000 characters!").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Spoofs a Minecraft achievement with the given message\n`" + Core.PREFIX + getInvoke() + " [message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "achievement";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"minecraftachievement", "mcachievement", "achieve"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}