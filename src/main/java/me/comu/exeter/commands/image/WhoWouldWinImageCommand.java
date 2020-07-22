package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
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

public class WhoWouldWinImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getMentionedMembers().size() != 2)
        {
            event.getChannel().sendMessage("Please specify two people to create a Who-Would-Win").queue();
            return;
        }
        String url = "https://nekobot.xyz/api/imagegen?type=whowouldwin&user1=" + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl() + "&user2=" + event.getMessage().getMentionedMembers().get(1).getUser().getEffectiveAvatarUrl();
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


    @Override
    public String getHelp() {
        return "Makes a who-would-win template with the specified users\n`" + Core.PREFIX + getInvoke() + " [user-1] <user-2>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "whowouldwin";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"whodwin", "www"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}