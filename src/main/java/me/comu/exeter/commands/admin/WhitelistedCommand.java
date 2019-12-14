package me.comu.exeter.commands.admin;

import me.comu.exeter.commands.admin.AntiRaidWhitelistCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class WhitelistedCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)/* || !member.canInteract(target)*/) {
            event.getChannel().sendMessage("You don't have permission to see the whitelisted users").queue();
            return;
        }
        if (AntiRaidWhitelistCommand.whitelistedIDs.isEmpty())
        {
            event.getChannel().sendMessage("null").queue();
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String x : AntiRaidWhitelistCommand.whitelistedIDs)
        {
           Member member = event.getGuild().getMemberById(x);
           String name = member.getUser().getName() + "#" + member.getUser().getDiscriminator();
         stringBuffer.append(" + " + name + "\n");
        }

        event.getChannel().sendMessage(EmbedUtils.embedMessage("Whitelisted Users: (BETA)\n" + stringBuffer.toString()).build()).queue();


    }

    @Override
    public String getHelp() {
        return "See all the users on the whitelist\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "whitelisted";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"seewhitelist","arwhitelisted","whitelistlist"};
    }
}
