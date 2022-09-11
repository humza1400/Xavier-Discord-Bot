package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BlacklistedCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to see the blacklisted users.").build()).queue();
            return;
        }
        if (BlacklistCommand.getBlacklistedUsers().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("There are no users blacklisted.").build()).queue();
            return;
        }
        if (event.getAuthor().getIdLong() == Core.OWNERID && !args.isEmpty() && args.get(0).equals("-g")) {
            StringBuilder globalStringBuffer = new StringBuilder();
            int counter = 0;
            for (String x : BlacklistCommand.getBlacklistedUsers().keySet()) {
                String name = String.format(" (%s)", event.getJDA().getGuildById(BlacklistCommand.getBlacklistedUsers().get(x)));
                globalStringBuffer.append(" + ").append(name).append("\n");
                counter++;
            }
            event.getChannel().sendMessageEmbeds(Utility.embedMessage("**" + counter + " Blacklisted Users: (GLOBAL)**\n" + globalStringBuffer).setColor(Core.getInstance().getColorTheme()).build()).queue();
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        int counter2 = 0;
        for (String x : BlacklistCommand.getBlacklistedUsers().keySet()) {
            if (BlacklistCommand.getBlacklistedUsers().get(x).equals(event.getGuild().getId())) {
                stringBuffer.append(" + ").append(x).append("\n");
                counter2++;
            }
        }

        event.getChannel().sendMessageEmbeds(Utility.embedMessage("**" + counter2 + " Blacklisted Users: (LOCAL)**\n" + stringBuffer).setColor(Core.getInstance().getColorTheme()).build()).queue();

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
        return new String[]{"seeblacklist", "checkblacklist", "listblacklist", "blacklistconfig", "blacklistcfg", "bld"};
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
