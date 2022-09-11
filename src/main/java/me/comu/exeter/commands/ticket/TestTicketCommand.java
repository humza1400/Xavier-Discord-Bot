package me.comu.exeter.commands.ticket;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.objects.Ticket;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;


public class TestTicketCommand implements ICommand {

    public static HashMap<String, String> ticketCategory = new HashMap<>();
    public static HashMap<String, String> ticketMessage = new HashMap<>();
    public static final Map<WhitelistKey, Ticket> openGuildTickets = Collections.checkedMap(new HashMap<>(), WhitelistKey.class, Ticket.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        System.out.println(Ticket.tickets);
    }


    @Override
    public String getHelp() {
        return "Creates a Ticket Manager Instance\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "testtick";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public ICommand.Category getCategory() {
        return Category.TICKET;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}



