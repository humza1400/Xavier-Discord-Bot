package me.comu.exeter.commands.image;

import me.comu.exeter.utility.Config;
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
import java.util.concurrent.TimeUnit;

public class DistractBfImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty() || event.getMessage().getMentionedMembers().size() != 2)
        {
            event.getChannel().sendMessage("Please specify a message").queue();
            return;
        }
        String content = "user1=" + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl() + "&user2=" + event.getMessage().getMentionedMembers().get(1).getUser().getEffectiveAvatarUrl();
        String url = "https://nekobot.xyz/api/imagegen?type=ship&" + content;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { Config.clearCacheDirectory();
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Something went wrong making a request to the endpoint").build()).queue();
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
                    ImageIO.write(img, url.endsWith("gif") ? "gif" : "png", file);
                    event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
                } else {
                    Config.clearCacheDirectory();
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Something went wrong making a request to the endpoint").build()).queue();
                }

            }
        });


    }


    @Override
    public String getHelp() {
        return "Makes a distracted boyfriend template meme with the provided user's pfps\n`" + Core.PREFIX + getInvoke() + " [user-1] <userr-2>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "distractedbf";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"distractedboyfriend","distractbf","boyfriend"};
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