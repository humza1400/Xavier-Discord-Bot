package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ShortenURLCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedImage(Objects.requireNonNull(event.getGuild().getIconUrl()).concat("?size=256&f=.gif")).setColor(Objects.requireNonNull(event.getMember()).getColor()).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shortens the specified URL\n`" + Core.PREFIX + getInvoke() + " [URL]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "shortenurl";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bitly", "goo.gl","bit.ly","shorturl","urlshorten"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
