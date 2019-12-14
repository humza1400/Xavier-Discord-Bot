package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class DiscriminatorCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
    
    }

    @Override
    public String getHelp() {
        return "Used for getting a list of users or bots with the same discriminator\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "discrim";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"discriminator","tag"};
    }
}
