package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.WhitelistedJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ARSaveConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        WhitelistedJSONHandler.saveWhitelistConfig();
        event.getChannel().sendMessage("Successfully saved config: `whitelisted.json`").queue();

    }

    @Override
    public String getHelp() {
        return "Force saves the current anti-raid config\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "arsave";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"arsavecfg","arsaveconfig","savewhitelist"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
