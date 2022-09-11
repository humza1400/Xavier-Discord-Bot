package me.comu.exeter.commands.image;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DidYouMeanImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev/didyoumean?top={0}&bottom={1}";
        if (args.size() != 2)
        {
            event.getChannel().sendMessage("Please insert two words you would like to interpolate into a dym meme").queue();
            return;
        }
        try {

            final HttpURLConnection con = (HttpURLConnection) new URL(api.replace("{0}", args.get(0)).replace("{1}", args.get(1))).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("\u0041\u0075\u0074\u0068\u006f\u0072\u0069\u007a\u0061\u0074\u0069\u006f\u006e", "\u0049\u0056\u0036\u005f\u0071\u0061\u002d\u0035\u006a\u0037\u0050\u0046\u0070\u0046\u0041\u0034\u0075\u006c\u0062\u0078\u0064\u004a\u0066\u0057\u0075\u0056\u004d\u0058\u0058\u005a\u0078\u006d\u0075\u0037\u0053\u0047\u0050\u006b\u0044\u0031");
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(con.getInputStream());
            BufferedImage img = ImageIO.read(imageInputStream);
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex)
        {
            Config.clearCacheDirectory();
            event.getChannel().sendMessage("The message content cannot be over 2000 characters!").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Makes a google image with a \"did you mean xyz\"\n`" + Core.PREFIX + getInvoke() + " [search-text] [dym-text]`\nAliases: `" + Arrays.deepToString(getAlias())+ "`";
    }

    @Override
    public String getInvoke() {
        return "didyoumean";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"dym"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
