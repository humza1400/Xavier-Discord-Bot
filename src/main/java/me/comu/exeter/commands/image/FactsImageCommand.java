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
import java.util.StringJoiner;

public class FactsImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev/facts?text=";
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a message you would like to interpolate into a facts meme").queue();
            return;
        }
        try {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String message = stringJoiner.toString().replaceAll(" ", "%20");
            final HttpURLConnection con = (HttpURLConnection) new URL(api + message).openConnection();
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
            ex.printStackTrace();
            event.getChannel().sendMessage("The message content cannot be over 2000 characters!").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Makes a facts meme template with the text provided\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias())+ "`";
    }

    @Override
    public String getInvoke() {
        return "facts";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"factify"};
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
