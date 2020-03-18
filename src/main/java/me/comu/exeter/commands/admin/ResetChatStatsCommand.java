package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.ChatTrackingManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ResetChatStatsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ( event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to reset the chat stats.").queue();
            return;
        }
        ChatTrackingManager.resetAllChatCredits();
        event.getChannel().sendMessage("Successfully reset **ALL** chat statistics").queue();
    }

    @Override
    public String getHelp() {
        return "Resets ALL chat statistics\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "resetchatstats";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"chatstatsreset"};}

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
