package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class MuteCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!SetMuteRoleCommand.isMuteRoleSet()) {
            channel.sendMessage("Please set a mute-role first `" + Core.PREFIX + "help muterole`").queue();
            return;
        }

        if (args.isEmpty() || mentionedMembers.isEmpty()) {
            channel.sendMessage("Please specify a user to mute").queue();
            return;
        }

        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.MANAGE_SERVER) && (!member.hasPermission(Permission.MANAGE_ROLES)) || !member.canInteract(target)) {
            channel.sendMessage("You don't have permission to mute someone").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) && (!selfMember.hasPermission(Permission.MANAGE_ROLES)) || !selfMember.canInteract(target)) {
            channel.sendMessage("I don't have permissions to mute that user").queue();
            return;
        }
        if (target.getRoles().contains(SetMuteRoleCommand.getMutedRole())) {
                channel.sendMessage("They're already muted bro, let it go").queue();
                return;
        }
        try {
            if (reason.equals("")) {
                event.getGuild().addRoleToMember(target, SetMuteRoleCommand.getMutedRole()).reason(String.format("Muted by %#s", event.getAuthor())).complete();
                channel.sendMessage(String.format("Muted %s", target.getAsMention())).queue();
            } else {
                event.getGuild().addRoleToMember(target, SetMuteRoleCommand.getMutedRole()).reason(String.format("Muted by %#s for %s", event.getAuthor(), reason)).complete();
                channel.sendMessage(String.format("Muted %s for `%s`", target.getAsMention(), reason)).queue();
            }
        } catch (HierarchyException ex) {
            channel.sendMessage("I cannot mute anyone whilst the mute role is at a higher precedent than my own").queue();
        }


    }

    @Override
    public String getHelp() {
        return "Mutes the specified user from interacting with voice/text channels\n`" + Core.PREFIX + getInvoke() + " [reason]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "mute";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"silence", "quiet", "stfu", "shhh"};
    }
}
