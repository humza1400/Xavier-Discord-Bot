package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlacklistCommand implements ICommand {

    public static HashMap<String, String> blacklistedUsers = new HashMap<String, String>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if ( event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to blacklist anyone").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessage("I don't have permissions to blacklist anyone").queue();
            return;
        }

        if (args.isEmpty() || event.getMessage().getMentionedMembers().isEmpty())
        {
            event.getChannel().sendMessage("Please specify a user to blacklist").queue();
            return;
        }
        if (!(event.getGuild().getSelfMember().canInteract(event.getMessage().getMentionedMembers().get(0))))
        {
            event.getChannel().sendMessage("You cannot blacklist that user").queue();
            return;
        }
        blacklistedUsers.put(event.getMessage().getMentionedMembers().get(0).getId(), event.getGuild().getId());
        event.getGuild().ban(event.getMessage().getMentionedMembers().get(0).getUser(), 0,"Blacklisted").queue();
        event.getChannel().sendMessage("Blacklisted " + event.getMessage().getMentionedMembers().get(0).getAsMention()).queue();

    }

    @Override
    public String getHelp() {
        return "Blacklists the specified user from the guild\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "blacklist";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"bl"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
