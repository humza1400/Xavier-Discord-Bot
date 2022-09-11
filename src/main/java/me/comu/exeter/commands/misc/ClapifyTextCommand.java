package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ClapifyTextCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert some text to clapify").queue();
            return;
        }
        if (event.getMessage().getContentRaw().toLowerCase().startsWith(Core.PREFIX + "clapify")) {
            String newMessage = event.getMessage().getContentRaw().replace(Core.PREFIX + "clapify", "").replaceAll(" ", ":clap:");
            newMessage += ":clap:";
            event.getMessage().delete().queue();
            event.getChannel().sendMessage(newMessage).queue();
        } else {
            String newMessage = event.getMessage().getContentRaw().replace(Core.PREFIX + "clap", "").replaceAll(" ", ":clap:");
            newMessage += ":clap:";
            event.getMessage().delete().queue();
            event.getChannel().sendMessage(newMessage).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Clapifies the given text.\n`" + Core.PREFIX + getInvoke() + " [text]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clapify";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"clap"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
