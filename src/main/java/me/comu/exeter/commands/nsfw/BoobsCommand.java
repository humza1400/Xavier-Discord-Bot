package me.comu.exeter.commands.nsfw;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BoobsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getChannel().isNSFW()) {
            event.getChannel().sendMessage("You can only use this command in NSFW channels").queue();
            return;
        }
        String petUrl;
        if (Math.random() > 0.6)
            petUrl = "https://nekos.life/api/v2/img/boobs";
        else
            petUrl = "https://nekos.life/api/v2/img/tits";


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url(petUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String url = jsonObject.toMap().get("url").toString();
                    if (args.isEmpty()) {
                        event.getChannel().sendMessage(EmbedUtils.embedImage(url).setColor(Objects.requireNonNull(event.getMember()).getColor()).build()).queue();
                    }
                } else {
                    event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                }

            }
        });

    }

    @Override
    public String getHelp() {
        return "Shows boobies (.Y.)\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "boob";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"boobs", "boobies", "tits", "titties"};
    }

    @Override
    public Category getCategory() {
        return Category.NSFW;
    }
}
