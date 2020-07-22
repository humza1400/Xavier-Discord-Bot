package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBlacklistCommand implements ICommand {

    public static List<String> commandBlacklist = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("725452437342912542")) {
            return;
        }
        if (args.isEmpty() || event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("Please mention a user to command-blacklist").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear")) {
            event.getChannel().sendMessage("Cleared **" + commandBlacklist.size() + "** users from the command blacklist!").queue();
            commandBlacklist.clear();
        }
        Member member = event.getMessage().getMentionedMembers().get(0);
        if (member.getIdLong() == Core.OWNERID) {
            event.getChannel().sendMessage("You can't command-blacklist the owner of the bot!").queue();
            return;
        }
        if (commandBlacklist.contains(member.getId())) {
            commandBlacklist.remove(member.getId());
            event.getChannel().sendMessage("Removed **" + Utility.removeMarkdown(member.getUser().getAsTag()) + "** from the command-blacklist.").queue();
        } else {
            commandBlacklist.add(member.getId());
            event.getChannel().sendMessage("Added **" + Utility.removeMarkdown(member.getUser().getAsTag()) + "** from the command-blacklist").queue();
        }


    }

    @Override
    public String getHelp() {
        return "Blacklists the specified users from using commands\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "commandblacklist";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"cblacklist", "cmdblacklist", "cmdsblacklist", "cbl"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
