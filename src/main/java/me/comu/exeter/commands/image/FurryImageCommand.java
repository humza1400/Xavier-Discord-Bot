package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FurryImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty() && event.getMessage().getAttachments().isEmpty()) {
            event.getChannel().sendMessage("Please specify an image to furry-ify").queue();
            return;
        }
        String url = "https://nekobot.xyz/api/imagegen?type=awooify&url=";
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
        {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().get().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) { Config.clearCacheDirectory();
                    event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = Objects.requireNonNull(response.body()).string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String url = jsonObject.toMap().get("message").toString();
                        BufferedImage img = ImageIO.read(new URL(url));
                        File file = new File("cache/downloaded.png");
                        ImageIO.write(img, "png", file);
                        event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
                    } else {
                        Config.clearCacheDirectory();
                        event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                    }

                }
            });
        }


    }


    @Override
    public String getHelp() {
        return "Makes a furry image with the specified image\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "furry";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"furrify","furryimage","furryimg"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}