package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ShowCreditMessagesCommand implements ICommand {

    public static boolean creditNotifications = true;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to toggle credit notifications.").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert a value").queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!creditNotifications) {
                creditNotifications = true;
                event.getChannel().sendMessage("Credit notification-messages will now be sent.").queue();
            } else
                event.getChannel().sendMessage("Credit notifications are already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (creditNotifications) {
                creditNotifications = false;
                event.getChannel().sendMessage("Credit notification-messages will no longer be sent.").queue();
            } else
                event.getChannel().sendMessage("Credit notifications are already disabled").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Toggles whether or not it should send messages when someone gets credits in chat\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }


    @Override
    public String getInvoke() {
        return "creditmessages";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"showcreditmessages","creditnotifications","creditnotifs","showcreditmsg","creditmsg","creditmsgs","showcreditmsgs"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}

