package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class BlacklistWordCommand implements ICommand {

    public static final List<String> blacklistedWords = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to blacklist words.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to manage messages.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a word or phrase to blacklist.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear")) {
            blacklistedWords.clear();
            event.getChannel().sendMessageEmbeds(Utility.embed("Clearing the blacklist word hash.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("list")) {
            event.getChannel().sendMessageEmbeds(Utility.embed(blacklistedWords.toString()).build()).queue();
            return;
        }

        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String message = stringJoiner.toString();
        blacklistedWords.add(message.toLowerCase());
        event.getChannel().sendMessageEmbeds(Utility.embed("Adding that word to the blacklisted words hash " + event.getMember().getAsMention()).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Deletes all messages sent as blacklisted words\n`" + Core.PREFIX + getInvoke() + " [word/clear/list]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "blacklistword";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bwword", "blword"};
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
