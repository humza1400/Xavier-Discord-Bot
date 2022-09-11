package me.comu.exeter.commands.nsfw;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FeetCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getChannel().isNSFW()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can only use this command in NSFW channels").build()).queue();
            return;
        }
        String petUrl;
        if (Math.random() > 0.6)
            petUrl = "https://nekos.life/api/v2/img/feet";
        else
            petUrl = "https://nekos.life/api/v2/img/erofeet";

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url(petUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Config.clearCacheDirectory();
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Something went wrong making a request to the endpoint").build()).queue();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    System.out.println(jsonResponse);
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String url = jsonObject.toMap().get("url").toString();
                    event.getChannel().sendMessageEmbeds(Utility.embedImage(url).setColor(Core.getInstance().getColorTheme()).build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Something went wrong making a request to the endpoint").build()).queue();
                }
            }
        });

    }

    @Override
    public String getHelp() {
        return "Shows sexy feet\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "feet";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"hentaifeet", "erofeet"};
    }

    @Override
    public Category getCategory() {
        return Category.NSFW;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
