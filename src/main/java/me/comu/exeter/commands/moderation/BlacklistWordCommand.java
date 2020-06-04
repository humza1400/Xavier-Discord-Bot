package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class BlacklistWordCommand implements ICommand {

    public static List<String> blacklistedWords = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to blacklist words").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("I don't have permissions to manage messages").queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert a word or phrase to blacklist").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear"))
        {
            blacklistedWords.clear();
            event.getChannel().sendMessage("Clearing the blacklist word hash").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("list"))
        {
            event.getChannel().sendMessage(blacklistedWords.toString()).queue();
            return;
        }

        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String message = stringJoiner.toString();
        blacklistedWords.add(message.toLowerCase());
        event.getChannel().sendMessage("Adding that word to the blacklisted words hash " + event.getMember().getAsMention()).queue();
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
}
