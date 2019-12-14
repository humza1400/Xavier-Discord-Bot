package me.comu.exeter.wrapper;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class Wrapper {

    public static void sendPrivateMessage(User user, String content) {
        if (!user.getId().equals("650802703949234185")) {
            user.openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage(content).queue();
            });
        }
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


}
