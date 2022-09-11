package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class SuggestCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!SetSuggestionChannelCommand.bound) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Suggestion channel not bound, " + Core.PREFIX + "help setsuggestions").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getMessage().delete().reason("Empty Suggestion").queue();
            return;
        }
        if (event.getChannel().getIdLong() == SetSuggestionChannelCommand.logChannelID) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(stringJoiner.toString()).setColor(Core.getInstance().getColorTheme()).setFooter("Suggested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue((message -> {
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
