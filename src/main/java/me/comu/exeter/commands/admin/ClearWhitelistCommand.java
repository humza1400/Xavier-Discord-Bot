package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClearWhitelistCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to clear the whitelisted users.").build()).queue();
            return;
        }
        WhitelistCommand.getWhitelistedIDs().clear();
        Core.getInstance().getWhitelistedHandler().getWhitelistedIDs().clear();
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleared all values in the whitelist hash.").build()).queue();
        Core.getInstance().getWhitelistedHandler().saveConfig();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
