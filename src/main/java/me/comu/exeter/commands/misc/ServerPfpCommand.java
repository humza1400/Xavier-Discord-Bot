package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerPfpCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(Objects.requireNonNull(event.getGuild().getIconUrl()).concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Returns the server icon\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "serverpfp";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"icon", "guildpfp","guildicon","servericon","spfp"};
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
