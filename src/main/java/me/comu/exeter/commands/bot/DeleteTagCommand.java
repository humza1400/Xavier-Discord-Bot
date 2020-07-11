package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class DeleteTagCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to delete tags, sorry bro").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert a valid tag to delete").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("all-tags") && (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("Successfully cleared all tags (" + CreateTagCommand.tags.size() + ")").queue();
            CreateTagCommand.tags.clear();
            return;
        } else {
            event.getChannel().sendMessage("You need administrator permissions to clear all tags").queue();
        }
        if (CreateTagCommand.tags.containsKey(args.get(0))) {
            CreateTagCommand.tags.remove(args.get(0));
            event.getChannel().sendMessage("Successfully removed `" + Utility.removeMentions(args.get(0)) + "`").queue();
        } else {
            event.getChannel().sendMessage("No tag exists with the name of `" + Utility.removeMentions(args.get(0)) + "`. Reference " + Core.PREFIX + "tag tag-list").queue();
        }


    }

    @Override
    public String getHelp() {
        return "Assigns a custom message to a tag\n`" + Core.PREFIX + getInvoke() + " [tag]/[all-tags] <content>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "deletetag";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"tagdelete"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
