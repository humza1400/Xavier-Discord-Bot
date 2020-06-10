package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class StreamCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to change the streaming status").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("stop"))
        {
            Core.jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        Core.jda.getPresence().setActivity(Activity.streaming(stringJoiner.toString(), "twitch.tv/souljaboy").asRichPresence());
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


