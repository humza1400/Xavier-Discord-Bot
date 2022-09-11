package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetSuggestionChannelCommand implements ICommand {

    public static boolean bound = false;
    public static long logChannelID;
    private static String channelName;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set the suggestion channel").build()).queue();
            return;
        }
        if (bound && !args.isEmpty() && (args.get(0).equalsIgnoreCase("null") || args.get(0).equalsIgnoreCase("nullify"))) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Unbound the current suggestion channel: `" + channelName + "`").build()).queue();
            bound = false;
            return;
        } else if (bound) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Suggestion channel already bound. Nullifying...").build()).queue();
        }
        TextChannel channel = event.getChannel();
        logChannelID = channel.getIdLong();
        channelName = channel.getName();
        event.getChannel().sendMessageEmbeds(Utility.embedMessage("Please use " + Core.PREFIX + "suggest [suggestion] to give your suggestion to the server!").setTitle("Suggestion Channel Set To #" + channelName).setColor(Core.getInstance().getColorTheme()).setFooter("Suggested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
        bound = true;
    }

    @Override
    public String getHelp() {
        return "Binds the current text-channel to send suggestion messages\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setsuggestions";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setsuggestionchannel", "setsuggestionschannel", "setsuggestion"};
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
