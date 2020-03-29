package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SayCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a message to say through the bot").queue();
        }
        else {
            event.getMessage().delete().queue();
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            String message = stringJoiner.toString();
            message = message.replaceAll("@everyone", "everyone").replaceAll("@here","here");
            if (message.contains(".gg/"))
            {
                event.getChannel().sendMessage("Your message contains blacklisted words").queue();
                return;
            }
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

  @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
