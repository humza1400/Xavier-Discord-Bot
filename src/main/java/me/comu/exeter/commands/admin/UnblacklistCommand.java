package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnblacklistCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to un-blacklist anyone.").build()).queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to specify a user to un-blacklist.").build()).queue();
            return;
        }
        if (BlacklistCommand.getBlacklistedUsers().containsKey(args.get(0)))
        {
            BlacklistCommand.getBlacklistedUsers().remove(args.get(0));
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully Removed `" + args.get(0) + "` from the blacklist hash.").build()).queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (memberList.isEmpty()) {
            String id = args.get(0);
            try {
                Member member = event.getGuild().getMemberById(Long.parseLong(id));
                if (!BlacklistCommand.getBlacklistedUsers().containsKey(id))
                {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not blacklisted!").build()).queue();
                    return;
                }
                BlacklistCommand.getBlacklistedUsers().remove(id);
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully removed `" + Objects.requireNonNull(member).getUser().getName() + "#" + member.getUser().getDiscriminator() + "` from the blacklist").build()).queue();
            } catch (Exception ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid ID + " + Utility.removeMentions(id) + ".").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
