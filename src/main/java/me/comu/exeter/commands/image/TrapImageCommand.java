package me.comu.exeter.commands.image;

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

public class TrapImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getMentionedMembers().isEmpty())
        {
            event.getChannel().sendMessage("Please specify a user to trap").queue();
            return;
        }
        String url = "https://nekobot.xyz/api/imagegen?type=trap&image="+ Objects.requireNonNull(event.getMember()).getUser().getEffectiveAvatarUrl() +"&name=" + event.getMessage().getMentionedMembers().get(0).getUser().getName() + "&author=" + Objects.requireNonNull(event.getMember()).getUser().getName();
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


    @Override
    public String getHelp() {
        return "Traps the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "trap";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"trapcard"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}