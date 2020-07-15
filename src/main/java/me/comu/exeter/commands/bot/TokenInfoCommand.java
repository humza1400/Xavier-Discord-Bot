package me.comu.exeter.commands.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TokenInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a token").queue();
            return;
        }
        // oauth shit i'll probs never implement
        if (args.get(0).equalsIgnoreCase("oauth")) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("grant_type", "client_credentials")
                    .addFormDataPart("scope", "identify connections")
                    .addFormDataPart("client_id", event.getJDA().getSelfUser().getId())
                    .addFormDataPart("client_secret", "")
                    .build();
            Request botRequest = new Request.Builder().url("https://discord.com/api/v6/oauth2/token")
                    .post(requestBody)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            try (Response response = okHttpClient.newCall(botRequest).execute()){
                if (response.code() == 200) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    event.getChannel().sendMessage(MarkdownUtil.codeblock("Printed oAuth information", jsonResponse)).queue();
                } else {
                    event.getChannel().sendMessage("Somethign went wrong").queue();
                }

            } catch (IOException ex) {
                event.getChannel().sendMessage("Connection Throttled When Making Request").queue();
            }
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request botRequest = new Request.Builder().url("https://canary.discordapp.com/api/v7/users/@me").get()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bot " + args.get(0))
                    .build();
            try (Response response = okHttpClient.newCall(botRequest).execute()){
                if (response.code() == 200) {
                    String jsonResponse = Objects.requireNonNull(response.body()).string();
                    JsonParser parser = new JsonParser();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonElement el = parser.parse(jsonResponse);
                    jsonResponse = gson.toJson(el);
                    event.getChannel().sendMessage(MarkdownUtil.codeblock("json", jsonResponse)).queue();
                } else {
                    Request userRequest = new Request.Builder().url("https://canary.discordapp.com/api/v7/users/@me").get()
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", args.get(0))
                            .build();
                    Response response1 = okHttpClient.newCall(userRequest).execute();
                    if (response1.code() == 200) {
                        String jsonResponse = Objects.requireNonNull(response1.body()).string();
                        JsonParser parser = new JsonParser();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonElement el = parser.parse(jsonResponse);
                        jsonResponse = gson.toJson(el);
                        event.getChannel().sendMessage(MarkdownUtil.codeblock("json", jsonResponse)).queue();
                    } else {
                        event.getChannel().sendMessage("Token Couldn't Be Resolved | " + response1.code() + " " + response1.message()).queue();
                    }
                }

            } catch (IOException ex) {
                event.getChannel().sendMessage("Connection Throttled When Making Request").queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Shows information about the specified token\n`" + Core.PREFIX + getInvoke() + " [token]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "tokeninfo";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
