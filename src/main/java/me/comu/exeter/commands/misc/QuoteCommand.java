package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class QuoteCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(getQuote()).setColor(Core.getInstance().getColorTheme()).build()).queue();

        } else {
            event.getChannel().sendMessageEmbeds(Utility.embed(getQuote()).build()).queue();

        }
    }

    private String getQuote() {
        try {
            final URL link = new URL("https://inspirobot.me/api?generate=true");
            final BufferedReader input = new BufferedReader(new InputStreamReader(link.openStream()));
            input.close();
            return input.readLine();
        } catch (IOException ex) {
            return "Something went wrong! (IOException)";
        }
    }

    @Override
    public String getHelp() {
        return "Returns a quote\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "quote";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"getquote"};
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
