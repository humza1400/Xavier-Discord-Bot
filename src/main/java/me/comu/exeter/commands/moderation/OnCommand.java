package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class OnCommand implements ICommand {

    public static String userID;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user to turn on").queue();
            return;
        }
        if (!OffCommand.shouldDelete)
        {
            event.getChannel().sendMessage("No user is currently turned off.").queue();
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            OffCommand.shouldDelete = false;
            userID = targets.get(0).getId();
            event.getChannel().sendMessage("Ok, Turned on **" + targets.get(0).getAsMention() + "**.").queue();
            return;
        } else if (!args.isEmpty() && !mentionedMembers.isEmpty()) {
            OffCommand.shouldDelete = false;
            userID = mentionedMembers.get(0).getId();
            event.getChannel().sendMessage("Ok, Turned on **" + mentionedMembers.get(0).getAsMention() + "**.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Turns an off'd user back on\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "on";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"enable","reenable"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
