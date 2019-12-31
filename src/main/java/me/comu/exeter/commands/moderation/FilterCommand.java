package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class FilterCommand implements ICommand {


    private static boolean active = true;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to toggle the filter").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                event.getChannel().sendMessage("Filter is now active").queue();
            } else
                event.getChannel().sendMessage("Filter is already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessage("Filter is no longer active").queue();
            } else
                event.getChannel().sendMessage("Filter is already disabled").queue();
        }
}

    public static boolean isActive() {
        return active;
    }

    @Override
    public String getHelp() {
        return "Toggles a text-channel filter\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "filter";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"togglefilter"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
