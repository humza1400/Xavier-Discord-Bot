package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class BanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();


        if (!member.hasPermission(Permission.BAN_MEMBERS) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to ban that user").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have permissions to ban users").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Please specify a valid user to ban").queue();
            return;
        }
        String reason = String.join(" ", args.subList(1, args.size()));
        if (mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone","everyone").replaceAll("@here","here")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            Member target = targets.get(0);
            if (reason.equals("")) {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                    return;

                }
                if (!event.getMember().canInteract(target)) {
                    event.getChannel().sendMessage("You don't have permission to ban that user").queue();
                    return;
                }
                event.getGuild().ban(target, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
                channel.sendMessage(String.format("Banned %s", target.getUser().getName() + "#" + target.getUser().getDiscriminator())).queue();
            } else {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                    return;
                }
                if (!event.getMember().canInteract(target)) {
                    event.getChannel().sendMessage("You don't have permission to ban that user").queue();
                    return;
                }
                event.getGuild().ban(target, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
                channel.sendMessage(String.format("Banned %s for `%s`", target.getUser().getName() + "#" + target.getUser().getDiscriminator(), reason)).queue();
            }
            return;
        }
        Member target = mentionedMembers.get(0);
        if (reason.equals("")) {
            if (!selfMember.canInteract(target)) {
                event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                return;
            }
            if (!event.getMember().canInteract(target)) {
                event.getChannel().sendMessage("You don't have permission to ban that user").queue();
                return;
            }
            event.getGuild().ban(target, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
            channel.sendMessage(String.format("Banned %s", target.getUser().getName() + "#" + target.getUser().getDiscriminator())).queue();
        } else {
            if (!selfMember.canInteract(target)) {
                event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                return;
            }
            if (!event.getMember().canInteract(target)) {
                event.getChannel().sendMessage("You don't have permission to ban that user").queue();
                return;
            }
            event.getGuild().ban(target, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
            channel.sendMessage(String.format("Banned %s for `%s`", target.getUser().getName() + "#" + target.getUser().getDiscriminator(), reason)).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Bans the specified user\n" + "`" + Core.PREFIX + getInvoke() + " [user] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ban";
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
