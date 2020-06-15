package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SmugCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String petUrl = "https://nekos.life/api/v2/img/smug";

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url(petUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String url = jsonObject.toMap().get("url").toString();
                    if (args.isEmpty()) {
                        event.getChannel().sendMessage(EmbedUtils.embedImage(url).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** smugs at themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
                        return;
                    }

                    List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                    if (!args.isEmpty() && mentionedMembers.isEmpty()) {
                        List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
                        if (targets.isEmpty()) {
                            event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
                            return;
                        } else if (targets.size() > 1) {
                            event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                            return;
                        }
                        event.getChannel().sendMessage(EmbedUtils.embedImage(url).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** smugs at **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
                    } else if (!args.isEmpty()) {
                        event.getChannel().sendMessage(EmbedUtils.embedImage(url).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** smugs at **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
                    }
                } else {
                    event.getChannel().sendMessage("Something went wrong making a request to the endpoint").queue();
                }
            }
        });

    }

    @Override
    public String getHelp() {
        return "Smugs at the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "smug";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
