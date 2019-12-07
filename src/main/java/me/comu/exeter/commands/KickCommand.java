package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (args.isEmpty() || mentionedMembers.isEmpty()) {
            channel.sendMessage("Please specify a valid user to kick").queue();
            return;
        }

        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to kick someone").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.KICK_MEMBERS) || !selfMember.canInteract(target)) {
            channel.sendMessage("I don't have permissions to kick that user").queue();
            return;
        }

        if (reason.equals("")) {
            event.getGuild().kick(target, String.format("Kicked by %#s", event.getAuthor())).queue();
            channel.sendMessage(String.format("Kicked %s", target.getEffectiveName())).queue();
        }
        else { event.getGuild().kick(target, String.format("Kicked by %#s for %s", event.getAuthor(), reason)).queue();
            channel.sendMessage(String.format("Kicked %s for %s", target.getEffectiveName(), reason)).queue();}


    }

    @Override
    public String getHelp() {
        return "Kicks the specified user\n" +
                "`"  + Core.PREFIX + getInvoke() + " [user] <reason>`\nnAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
