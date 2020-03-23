package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class UnmuteCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (args.isEmpty()) {
            channel.sendMessage("Please specify a valid user to unmute").queue();
            return;
        }

        if (!member.hasPermission(Permission.MANAGE_ROLES) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to unmute someone").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MANAGE_ROLES)) {
            channel.sendMessage("I don't have permissions to unmute that user").queue();
            return;
        }
        if (mentionedMembers.isEmpty() && !args.isEmpty())
        {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty())
            {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone","everyone").replaceAll("@here","here")).queue();
                return;
            } else if (targets.size() > 1)
            {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            Member target = targets.get(0);
            if (target.getRoles().contains(SetMuteRoleCommand.getMutedRole())) {
                event.getGuild().removeRoleFromMember(target, SetMuteRoleCommand.getMutedRole()).reason("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).queue();
                channel.sendMessage("Unmuted "+ target.getAsMention()).queue();
            } else {
                event.getChannel().sendMessage("That user is not muted").queue();
            }
            return;
        }
        Member target = mentionedMembers.get(0);
        if (target.getRoles().contains(SetMuteRoleCommand.getMutedRole())) {
            event.getGuild().removeRoleFromMember(target, SetMuteRoleCommand.getMutedRole()).reason("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).queue();
            channel.sendMessage("Unmuted "+ target.getAsMention()).queue();
        } else {
            event.getChannel().sendMessage("That user is not muted").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Unmutes a muted user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unmute";
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
