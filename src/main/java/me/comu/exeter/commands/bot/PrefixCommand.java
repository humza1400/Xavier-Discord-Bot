package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class PrefixCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to change the prefix, sorry bro.").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a prefix.").build()).queue();
            return;
        }
        Core.PREFIX = args.get(0);
        MessageEmbed embed = new EmbedBuilder().addField("Prefix set to", args.get(0), false).setColor(Core.getInstance().getColorTheme()).build();
        event.getChannel().sendMessageEmbeds(embed).queue();
    }

    @Override
    public String getHelp() {
        return "Sets the bot's prefix (Current Prefix `" + Core.PREFIX + "`)\n`" + Core.PREFIX + getInvoke() + "`n\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }


    @Override
    public String getInvoke() {
        return "prefix";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
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

