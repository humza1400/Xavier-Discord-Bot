package me.comu.exeter.commands.image;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KmsImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty() && event.getMessage().getAttachments().isEmpty()) {
            event.getChannel().sendMessage("Please specify an image to kms-ify").queue();
            return;
        }
        String url = "https://nekobot.xyz/api/imagegen?type=kms&url=";
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            url = url + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl();
        } else if (!args.isEmpty() && !Utility.isUrl(args.get(0))) {
            event.getChannel().sendMessage("I couldn't resolve the specified URL").queue();
            return;
        }
        if (!args.isEmpty() && Utility.isUrl(args.get(0))) {
            url = url + args.get(0);
        } else if (!event.getMessage().getAttachments().isEmpty()) {
            url = url + event.getMessage().getAttachments().get(0).getUrl();
        }
        {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().get().url(url).build();
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
                        String url = jsonObject.toMap().get("message").toString();
                        event.getChannel().sendMessage(EmbedUtils.embedImage(url).setColor(Objects.requireNonNull(event.getMember()).getColor()).build()).queue();
                    } else {
                        event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                    }

                }
            });
        }

    }


    @Override
    public String getHelp() {
        return "Makes the specified image into a kms template\n`" + Core.PREFIX + getInvoke() + " [image]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kms";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"kmsimage","kmsimg"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}