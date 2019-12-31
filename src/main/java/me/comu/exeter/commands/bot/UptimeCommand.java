package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Arrays;
import java.util.List;

public class UptimeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;

        event.getChannel().sendMessageFormat(
                "Uptime: `%s hours, %s minutes, %s seconds`",
                numberOfHours, numberOfMinutes, numberOfSeconds
        ).queue();
    }
    @Override
    public String getHelp() {
        return "Returns the uptime of the bot\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "uptime";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"runtime","upkeep","alive","online"};
    }

  @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
