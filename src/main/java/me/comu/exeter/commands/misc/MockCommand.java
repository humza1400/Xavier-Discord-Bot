package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MockCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert some text to mock").queue();
            return;
        }
        StringBuilder message = new StringBuilder();
        for (String msg : args)
        {
            message.append(msg).append(" ");
        }
        String newMessage = "";
        for (char letter : message.toString().toCharArray()) {
            if (Math.random() > .50) {
                newMessage += (Character.toUpperCase(letter));
            } else {
                newMessage += (letter);
            }
        }
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(newMessage).queue();
    }

    @Override
    public String getHelp() {
        return "Mocks the given text.\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "mock";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
