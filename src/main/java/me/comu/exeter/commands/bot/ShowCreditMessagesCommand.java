package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ShowCreditMessagesCommand implements ICommand {

    public static boolean creditNotifications = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle credit notifications.").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a value.").build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!creditNotifications) {
                creditNotifications = true;
                event.getChannel().sendMessageEmbeds(Utility.embed("Credit notification-messages will now be sent.").build()).queue();
            } else
                event.getChannel().sendMessage("Credit notifications are already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (creditNotifications) {
                creditNotifications = false;
                event.getChannel().sendMessageEmbeds(Utility.embed("Credit notification-messages will no longer be sent.").build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(Utility.embed("Credit notifications are already disabled.").build()).queue();
            }

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

    @Override
    public boolean isPremium() {
        return false;
    }
}

