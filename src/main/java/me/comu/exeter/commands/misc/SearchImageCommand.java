package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SearchImageCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify what image you want to search for").queue();
            return;
        }
        final String key = "AIzaSyAls9zrVVQtZksm-tMrKLhmXx3T1hrt_5c";



    }

    @Override
    public String getHelp() {
        return "Searches google for images\n`" + Core.PREFIX + getInvoke() + " [image-search]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "imagesearch";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"image","img","searchimage"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
