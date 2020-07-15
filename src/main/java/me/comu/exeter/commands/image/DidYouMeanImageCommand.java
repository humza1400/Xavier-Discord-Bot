package me.comu.exeter.commands.image;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class DidYouMeanImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final String api = "https://api.alexflipnote.dev/didyoumean?top={0}&bottom={1}";
        if (args.size() != 2)
        {
            event.getChannel().sendMessage("Please insert a message you would like to interpolate into a dym meme").queue();
            return;
        }
        try {
            event.getChannel().sendMessage(EmbedUtils.embedImage(api.replace("{0}", args.get(0)).replace("{1}", args.get(1))).build()).queue();
        } catch (IllegalArgumentException ex)
        {
            event.getChannel().sendMessage("The message content cannot be over 2000 characters!").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Makes a google image with a \"did you mean xyz\"\n`" + Core.PREFIX + getInvoke() + " [search-text] [dym-text]`\nAliases: `" + Arrays.deepToString(getAlias())+ "`";
    }

    @Override
    public String getInvoke() {
        return "didyoumean";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"dym"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
