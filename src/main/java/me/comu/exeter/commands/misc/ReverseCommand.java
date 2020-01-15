package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ReverseCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert some text to flip").queue();
            return;
        }
        if (event.getMessage().getContentRaw().toLowerCase().startsWith(Core.PREFIX + "reverse"))
        {
            event.getMessage().delete().queue();
            String msg = event.getMessage().getContentRaw().replace(Core.PREFIX + "reverse", "");
            event.getChannel().sendMessage(reverseText(msg)).queue();
        } else {
            event.getMessage().delete().queue();
            String msg = event.getMessage().getContentRaw().replace(Core.PREFIX + "flip", "");
            event.getChannel().sendMessage(reverseText(msg)).queue();
        }
    }

    private String reverseText(String text)
    {
        StringBuilder buffer = new StringBuilder();
        for(int i = text.length() - 1; i >= 0; i--)
        {
            buffer.append(text.charAt(i));
        }
        return buffer.toString();
    }

    @Override
    public String getHelp() {
        return "Reverses the given text.\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "reverse";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"flip"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
