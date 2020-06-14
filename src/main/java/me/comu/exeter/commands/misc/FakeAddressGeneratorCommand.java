package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class FakeAddressGeneratorCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        event.getChannel().sendMessage(".").queue();
    }

    @Override
    public String getHelp() {
        return "Returns a fake address\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "fakeaddress";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"genaddress","genaddy","fakeaddy"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
