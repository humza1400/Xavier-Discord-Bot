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
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class ClydeImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a message").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String content = stringJoiner.toString();
        String url = "https://nekobot.xyz/api/imagegen?type=clyde&text=" + content;
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
        return "Makes a fake message with the Discord Clyde Bot\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clyde";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
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