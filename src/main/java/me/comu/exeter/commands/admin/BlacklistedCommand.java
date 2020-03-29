package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BlacklistedCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to see the blacklisted users").queue();
            return;
        }
        if (BlacklistCommand.blacklistedUsers.isEmpty()) {
            event.getChannel().sendMessage("null").queue();
            return;
        }
        if (event.getAuthor().getIdLong() == Core.OWNERID && !args.isEmpty() && args.get(0).equals("-g")) {
            StringBuilder globalStringBuffer = new StringBuilder();
            int counter = 0;
            for (String x : BlacklistCommand.blacklistedUsers.keySet()) {
                    String name = String.format(" (%s)", event.getJDA().getGuildById(BlacklistCommand.blacklistedUsers.get(x)));
                    globalStringBuffer.append(" + ").append(name).append("\n");
                    counter++;
            }
            event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + counter + " Blacklisted Users: (GLOBAL)**\n" + globalStringBuffer.toString()).build()).queue();
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        int counter2 = 0;
        for (String x : BlacklistCommand.blacklistedUsers.keySet()) {
            if (BlacklistCommand.blacklistedUsers.get(x).equals(event.getGuild().getId())) {
                stringBuffer.append(" + ").append(x).append("\n");
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
