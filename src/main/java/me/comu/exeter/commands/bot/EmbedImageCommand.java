package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmbedImageCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify an image-url to embed").queue();
            return;
        }
        String url = args.get(0);
        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS))
        {
            try {
                event.getChannel().sendMessage(EmbedUtils.embedImage(url).setColor(0).build()).queue();
            } catch (IllegalArgumentException ex)
            {
                event.getChannel().sendMessage("Invalid iamge-url, please try again with a working URL.").queue();
            }
        } else {
            event.getChannel().sendMessage("No permission to embed images.").queue();

        }
    }

    @Override
    public String getHelp() {
        return "Embeds the image-url given\n`" + Core.PREFIX + getInvoke() + " [image-url]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "embedimage";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"imageembed","embedimg","imgembed","embedpic"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
