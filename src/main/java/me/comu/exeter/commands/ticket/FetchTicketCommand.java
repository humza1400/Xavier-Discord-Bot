package me.comu.exeter.commands.ticket;

import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class FetchTicketCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "fetchticket";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"ticket"};
    }

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
