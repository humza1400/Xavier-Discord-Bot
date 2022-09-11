package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class JailRecordCommand implements ICommand {

    private final String base = " http://www.JailBase.com/api/1/";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessageEmbeds(Utility.embed("Querying database... :gear:").build()).queue();

    }

    @Override
    public String getHelp() {
        return "Sets your account AFK\n`" + Core.PREFIX + getInvoke() + " [recent]/[search] <first-name> (last name)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "jailbase";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"jailrecords", "arrestrecord", "arrestrecords", "jailrecord"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
