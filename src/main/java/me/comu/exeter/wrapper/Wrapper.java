package me.comu.exeter.wrapper;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Wrapper {

//    public static List<Shard> shards = new ArrayList<>();

    public static void sendPrivateMessage(User user, String content) {
        if (!user.getId().equals("650802703949234185")) {
            user.openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage(content).queue();
            });
        }
    }

    public void sendPrivateMessageWithDelay(User user, String content, long delay, TimeUnit timeUnit) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queueAfter(delay, timeUnit);
        });
    }

    public static void sendWhitelistedAntiRaidInfoMessage(Guild guild, List<String> list, String message)
    {
        for (String user : list)
        {
            sendPrivateMessage(guild.getMemberById(user).getUser(), message);
        }
    }

    public static boolean botCheck(Message message)
    {
        if (message.getAuthor().isBot())
            return false;
        return true;
    }

    public static long timeToMS(int hours, int minutes, int seconds) {
        if(seconds > 59 || seconds < 0) {
            return -1;
        }
        if(minutes > 59 || minutes < 0) {
            return -1;
        }

        long s = (seconds + (60 * (minutes + (hours * 60))));
        return TimeUnit.SECONDS.toMillis(s);
    }
    public static List<Guild> getGuilds() {
        List<Guild> guilds = new ArrayList<>();
     //   for(Shard shard : shards) {
       //     guilds.addAll(shard.getJda().getGuilds());
        //}
        return guilds;
    }

    public static Color getRandomColor() {
        final Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }


}
