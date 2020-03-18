package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class BlacklistedCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to see the blacklisted users").queue();
            return;
        }
        if (BlacklistCommand.blacklistedUsers.isEmpty()) {
            event.getChannel().sendMessage("null").queue();
            return;
        }
        if (event.getAuthor().getIdLong() == Core.OWNERID && !args.isEmpty() && args.get(0).equals("-g")) {
            StringBuffer globalStringBuffer = new StringBuffer();
            int counter = 0;
            for (String x : BlacklistCommand.blacklistedUsers.keySet()) {
                    String name = String.format(" (%s)", event.getJDA().getGuildById(BlacklistCommand.blacklistedUsers.get(x)));
                    globalStringBuffer.append(" + " + name + "\n");
                    counter++;
            }
            event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + counter + " Blacklisted Users: (GLOBAL)**\n" + globalStringBuffer.toString()).build()).queue();
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int counter2 = 0;
        for (String x : BlacklistCommand.blacklistedUsers.keySet()) {
            if (BlacklistCommand.blacklistedUsers.get(x).equals(event.getGuild().getId())) {
                    String name = x;
                    stringBuffer.append(" + " + name + "\n");
                    counter2++;
            }
        }

        event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + counter2 + " Blacklisted Users: (LOCAL)**\n" + stringBuffer.toString()).build()).queue();

    }

    @Override
    public String getHelp() {
        return "See all the users on the blacklist\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "blacklisted";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"seeblacklist", "checkblacklist", "listblacklist","blacklistconfig","blacklistcfg", "bld"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
