package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        long time = System.currentTimeMillis();
        event.getChannel().sendMessageEmbeds(Utility.embed("Ping: ").build()).queue((response ->
                response.editMessageEmbeds(Utility.embed(String.format("Ping: **%dms**", System.currentTimeMillis() - time)).build()).queue()));
    }

    @Override
    public String getHelp() {
        return "Gets the bot's Latency.\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
