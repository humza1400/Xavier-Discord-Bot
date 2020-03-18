package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SuggestCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!SetSuggestionChannelCommand.bound) {
            event.getChannel().sendMessage("Suggestion channel not bound, " + Core.PREFIX + "help setsuggestions").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getMessage().delete().reason("Empty Suggestion").queue();
            return;
        }
        if (event.getChannel().getIdLong() == SetSuggestionChannelCommand.logChannelID) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            event.getChannel().sendMessage(EmbedUtils.embedMessage(stringJoiner.toString()).setColor(Color.BLUE).setFooter("Suggested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl()).build()).queue((message -> {
                event.getChannel().addReactionById(message.getId(), "U+1F44D").queue();
                event.getChannel().addReactionById(message.getId(), "U+1F44E").queue();
            }));

        }
    }

    @Override
    public String getHelp() {
        return "Gives a suggestion that should be added to the server\n`" + Core.PREFIX + getInvoke() + " [suggestion]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "suggest";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"suggestion", "recommend", "givesuggestion"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
