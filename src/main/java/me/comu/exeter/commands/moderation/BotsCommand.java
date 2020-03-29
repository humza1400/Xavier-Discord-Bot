package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BotsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        StringBuilder buffer = new StringBuilder();
        List<Member> bots = event.getGuild().getMembers().stream().filter(member -> member.getUser().isBot()).sorted(Comparator.comparing(Member::getTimeJoined)).collect(Collectors.toList());
        int count = 1;
        for (Member m : bots) {
            buffer.append(count++).append(". ").append(m.getAsMention()).append("\n");
        }
        event.getChannel().sendMessage("Total bots: `" + bots.size() + "`\n" + buffer.toString()).queue();
    }

    @Override
    public String getHelp() {
        return "Lists all the bots in the server\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "bots";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
