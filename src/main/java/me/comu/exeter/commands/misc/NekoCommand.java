package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class NekoCommand implements ICommand {

    private final String nekkobotBase = "https://nekobot.xyz/api/image?type=";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

    }

    @Override
    public String getHelp() {
        return "For all you cat-loving furries out there\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "neko";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"furryporn","catporn","cat","kitty","hentai"};
    }

     @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
