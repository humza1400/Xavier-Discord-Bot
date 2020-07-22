package me.comu.exeter.commands.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DogCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            BufferedImage img = ImageIO.read(new URL(getCatUrl()));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex)
        {
            Config.clearCacheDirectory();
            event.getChannel().sendMessage("Somethign went wrong, try again later").queue();
        }
    }

    private String getCatUrl() {
        try {
            JsonArray jsonArray = Utility.getJsonFromURL("https://api.thedogapi.com/v1/images/search").getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            return jsonObject.get("url").getAsString();
        } catch (IOException ex) {
            return "Something went wrong! (IOException)";
        }
    }

    @Override
    public String getHelp() {
        return "Returns a dog\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dog";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
