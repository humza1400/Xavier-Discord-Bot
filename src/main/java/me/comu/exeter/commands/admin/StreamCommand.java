package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class StreamCommand implements ICommand {

    public static boolean members;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().substring(7).equals("members"))
        {
            members = true;
            return;
        }
        members = false;
        Core.jda.getPresence().setActivity(Activity.streaming(event.getMessage().getContentRaw().substring(5), "twitch.tv/souljaboy").asRichPresence());
    }


    @Override
    public String getHelp() {
        return "Sets the streaming status for the bot\n" + "`" + Core.PREFIX + getInvoke() + " [status]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "stream";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}


