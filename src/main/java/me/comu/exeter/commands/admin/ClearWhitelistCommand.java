package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.WhitelistedJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClearWhitelistCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to clear the whitelisted users").queue();
            return;
        }
        WhitelistCommand.getWhitelistedIDs().clear();
        WhitelistedJSONHandler.whitelistedIDs.clear();
        event.getChannel().sendMessage("Successfully cleared all values in the whitelist hash").queue();
        WhitelistedJSONHandler.saveWhitelistConfig();
    }

    @Override
    public String getHelp() {
        return "Resets the whitelist hash for the current guild\n`" + Core.PREFIX + getInvoke() + "` [argument]\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clearwhitelist";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"emptywhitelist", "nullwhitelist", "nullifywhitelist", "resetwhitelist","purgewhitelist","whitelistclear","wlclear","clearwl","wldclear"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
