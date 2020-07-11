package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnblacklistCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to unblacklist anyone").queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("You need to specify a user to unblacklist").queue();
            return;
        }
        if (BlacklistCommand.blacklistedUsers.containsKey(args.get(0)))
        {
            BlacklistCommand.blacklistedUsers.remove(args.get(0));
            event.getChannel().sendMessage("Successfully Removed `" + args.get(0) + "` from the blacklist hash").queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (memberList.isEmpty()) {
            String id = args.get(0);
            try {
                Member member = event.getGuild().getMemberById(Long.parseLong(id));
                if (!BlacklistCommand.blacklistedUsers.containsKey(id))
                {
                    event.getChannel().sendMessage("That user is not blacklisted!").queue();
                    return;
                }
                BlacklistCommand.blacklistedUsers.remove(id);
                event.getChannel().sendMessage("Successfully removed `" + Objects.requireNonNull(member).getUser().getName() + "#" + member.getUser().getDiscriminator() + "` from the blacklist").queue();
            } catch (Exception ex) {
                event.getChannel().sendMessage("Invalid ID + " + Utility.removeMentions(id)).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Unblacklists the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unblacklist";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"removeblacklist","ubl"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
