package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ServerBannerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getGuild().getBannerUrl() == null) {
            event.getChannel().sendMessageEmbeds(Utility.embed("The server has no banner set.").build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(event.getGuild().getBannerUrl().concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Returns the server banner\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "serverbanner";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"guildbanner", "bannerpfp"};
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
