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

public class BanTagCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!member.hasPermission(Permission.BAN_MEMBERS) && member.getIdLong() != Core.OWNERID/* || !member.canInteract(target)*/) {
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
        User user = event.getJDA().getUserByTag(args.get(0));
        String reason = String.join(" ", args.subList(1, args.size()));
        if (user == null) {
            event.getChannel().sendMessage("Couldn't find the user with the given tag of `" + args.get(0) + "`".replaceAll("@everyone","everyone").replaceAll("@here","here")).queue();
            return;
        }
        if (mentionedMembers.isEmpty()) {
            Member target = event.getGuild().getMemberById(user.getId());
            if (reason.equals("")) {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                    return;

                }
                event.getGuild().ban(target, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
                channel.sendMessage(String.format("Banned %s", target.getUser().getName() + "#" + target.getUser().getDiscriminator())).queue();
            } else {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                    return;
                }
                event.getGuild().ban(target, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
                channel.sendMessage(String.format("Banned %s for `%s`", target.getUser().getName() + "#" + target.getUser().getDiscriminator(), reason)).queue();
            }
            return;
        }
    }

    @Override
    public String getHelp() {
        return "Bans the specified user-tag (swag#3231)\n" + "`" + Core.PREFIX + getInvoke() + " [tag] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "bantag";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"tagban"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
