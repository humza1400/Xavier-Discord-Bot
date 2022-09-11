package me.comu.exeter.commands.marriage;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FeedCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String petUrl = "https://nekos.life/api/v2/img/feed";

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
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String url = jsonObject.toMap().get("url").toString();
                    if (args.isEmpty()) {
                        event.getChannel().sendMessageEmbeds(Utility.embedImage(url).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** feeds themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
                        return;
                    }
                    List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                    if (!args.isEmpty() && mentionedMembers.isEmpty()) {
                        List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
                        if (targets.isEmpty()) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                            return;
                        } else if (targets.size() > 1) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Multiple users found! Try mentioning the user instead.").build()).queue();
                            return;
                        }
                        event.getChannel().sendMessageEmbeds(Utility.embedImage(url).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** feeds **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
                    } else if (!args.isEmpty()) {
                        event.getChannel().sendMessageEmbeds(Utility.embedImage(url).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** feeds **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
                    }
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Something went wrong making a request to the endpoint").build()).queue();
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
        return "feed";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
