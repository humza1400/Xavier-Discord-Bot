package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OwnerCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You must be the owner of the bot to transfer ownership.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            User user = event.getJDA().getUserById(Core.OWNERID);
            if (user == null)
            {
                event.getChannel().sendMessageEmbeds(Utility.embed("The current owner is `" + Core.OWNERID + "`.").build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(Utility.embed("The current owner is `" + user.getAsTag() + " (" + Core.OWNERID + ")`.").build()).queue();
            }
        }
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            User user = event.getJDA().getUserById(args.get(0));
            if (user == null) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find that user!").build()).queue();
            } else if (user.getIdLong() == Core.OWNERID) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You're already the owner of the bot!").build()).queue();
            } else {
                Core.OWNERID = user.getIdLong();
                event.getChannel().sendMessageEmbeds(Utility.embed("Transferred ownership to `" + Utility.removeMarkdown(user.getAsTag()) + "`").build()).queue();
            }
        } else {
            Member member = event.getMessage().getMentionedMembers().get(0);
            Long ownerID = Core.OWNERID;
            if (member.getIdLong() == ownerID) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You're already the owner of the bot").build()).queue();
            } else {
                Core.OWNERID = member.getIdLong();
                event.getChannel().sendMessageEmbeds(Utility.embed("Transferred ownership to `" + Utility.removeMarkdown(member.getUser().getAsTag()) + "`").build()).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Transfers ownership of the bot to the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "owner";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"transferownership", "transferowner", "botowner"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
