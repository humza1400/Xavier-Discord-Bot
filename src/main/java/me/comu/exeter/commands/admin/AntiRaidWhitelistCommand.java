package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AntiRaidWhitelistCommand implements ICommand {

    public static List<String> whitelistedIDs = new ArrayList<String>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("You need to specify a user to whitelist").queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (mentionedMembers.isEmpty()) {
            String id = args.get(0);
            try {
                Member member = event.getGuild().getMemberById(Long.parseLong(id));
                if (whitelistedIDs.contains(id))
                {
                    event.getChannel().sendMessage("User already whitelisted.").queue();
                    return;
                }
                whitelistedIDs.add(id);
                event.getChannel().sendMessage("Added " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " to the whitelist").queue();
            } catch (Exception ex) {
                event.getChannel().sendMessage("Invalid ID + " + id).queue();
            }
        } else {
            if (whitelistedIDs.contains(mentionedMembers.get(0).getId()))
            {
                event.getChannel().sendMessage("User already whitelisted.").queue();
                return;
            }
            whitelistedIDs.add(mentionedMembers.get(0).getId());
            event.getChannel().sendMessage("Added " + mentionedMembers.get(0).getUser().getName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator() + " to the whitelist").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Whitelists the specific user to the bot\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "whitelist";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"arwhitelist","artrust","antiraidtrust","antiraidwhitelist"};
    }
}
