package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SetServerIconCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }
            try {
                Icon icon = Icon.from(new File("C:/Users/player/Desktop/xia/discord-bot/dependencies/serverIcon.jpg"));
                event.getGuild().getManager().setIcon(icon).queue();
            }
            catch(IOException e) {
            }

    }

    @Override
    public String getHelp() {
        return "Sets the server icon to a poodle. Lol.\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "'";
    }

    @Override
    public String getInvoke() {
        return "changepfp";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"changeserverpfp","changeicon"};
    }
}
