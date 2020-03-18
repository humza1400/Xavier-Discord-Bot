package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ModMailCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert a message to send as Mod Mail!").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String message = stringJoiner.toString();
        for (Member member : event.getGuild().getMembers()) {
            if (member.hasPermission(Permission.ADMINISTRATOR) && !member.getUser().isBot()) {
                member.getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(message)).queue(null, failure -> event.getChannel().sendMessage(member.getUser().getAsTag() + " has their DMs disabled.").queue());
            }
        }
        event.getChannel().sendMessage("Successfully sent out a mod mail!\nExecuted by: " + event.getAuthor().getAsTag()).queue();
    }

    @Override
    public String getHelp() {
        return "Sends a direct message to all admins\n`" + Core.PREFIX + getInvoke() + " [message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "modmail";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.NUKE;
    }
}
