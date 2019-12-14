package me.comu.exeter.commands.bot;

import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class AddCustomCommandCommand  implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {


    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return null;
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
