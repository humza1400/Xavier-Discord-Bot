package me.comu.exeter.commands.image;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AmIAJokeImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        User user;
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            user = event.getAuthor();
        } else {
            user = event.getMessage().getMentionedMembers().get(0).getUser();
        }
        String url = "https://api.alexflipnote.dev/amiajoke?image=" + user.getEffectiveAvatarUrl();
        try {
            final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("\u0041\u0075\u0074\u0068\u006f\u0072\u0069\u007a\u0061\u0074\u0069\u006f\u006e", "\u0049\u0056\u0036\u005f\u0071\u0061\u002d\u0035\u006a\u0037\u0050\u0046\u0070\u0046\u0041\u0034\u0075\u006c\u0062\u0078\u0064\u004a\u0066\u0057\u0075\u0056\u004d\u0058\u0058\u005a\u0078\u006d\u0075\u0037\u0053\u0047\u0050\u006b\u0044\u0031");
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(con.getInputStream());
            BufferedImage img = ImageIO.read(imageInputStream);
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, url.endsWith("gif") ? "gif" : "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("The message can't be over 2,000 characters!").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}