package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SayCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!args.isEmpty()) {
            List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
            messages2.get(0).delete().queueAfter(3, TimeUnit.MILLISECONDS);
            String message = event.getMessage().getContentRaw();
            message = message.replaceAll("@everyone", "everyone");
            message = message.replaceAll("@here", "here");
            message = message.replaceAll(Core.PREFIX, "");
            message = message.substring(3);
            event.getChannel().sendMessage(message).queue();

        }
    }

    @Override
    public String getHelp() {
        return "Sends a message through the bot\n`" + Core.PREFIX + getInvoke() + " [message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "say";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
