package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.VCTrackingManager;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResetVCStatsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ( Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to reset the voice-channel stats.").build()).queue();
            return;
        }
        VCTrackingManager.resetAllJoinTimes();
        VCTrackingManager.resetAllLeaveTimes();
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully reset **ALL** voice-channel statistics.").build()).queue();
    }

    @Override
    public String getHelp() {
        return "Resets ALL voice-channel statistics\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "resetvcstats";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"vcstatsreset"};}

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
