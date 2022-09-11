package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class EmbedImageCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify an image-url to embed.").build()).queue();
            return;
        }
        String url = args.get(0);
        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS))
        {
            try {
                event.getChannel().sendMessageEmbeds(Utility.embedImage(url).setColor(0).build()).queue();
            } catch (IllegalArgumentException ex)
            {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid image-url, please try again with a working URL.").build()).queue();
            }
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission to embed images.").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
