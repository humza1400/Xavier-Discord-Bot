package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OffCommand implements ICommand {

    public static List<String> offedUsers = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to turn someone off").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("I don't have permissions to turn someone off").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user to turn off").queue();
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + Wrapper.removeMentions(args.get(0))).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            if (offedUsers.contains(targets.get(0).getId()))
            {
                event.getChannel().sendMessage(targets.get(0).getAsMention() + " is already turned off.").queue();
                return;
            }
            offedUsers.add(targets.get(0).getId());
            if (targets.get(0).getId().equals(event.getJDA().getSelfUser().getId()))
            {
                event.getChannel().sendMessage("You can't turn me off :(").queue();
                return;
            }
            event.getChannel().sendMessage("Ok, Turned off **" + targets.get(0).getAsMention() + "**.").queue();
        } else if (!args.isEmpty()) {
            if (offedUsers.contains(mentionedMembers.get(0).getId()))
            {
                event.getChannel().sendMessage(mentionedMembers.get(0).getAsMention() + " that user is already turned off.").queue();
                return;
            }
            offedUsers.add(mentionedMembers.get(0).getId());
            event.getChannel().sendMessage("Ok, Turned off **" + mentionedMembers.get(0).getAsMention() + "**.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Deletes all messages the off'd user tries to send\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "off";
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
