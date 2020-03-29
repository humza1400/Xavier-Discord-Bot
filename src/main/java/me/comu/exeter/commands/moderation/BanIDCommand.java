package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BanIDCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!Objects.requireNonNull(member).hasPermission(Permission.BAN_MEMBERS) && member.getIdLong() != Core.OWNERID/* || !member.canInteract(target)*/) {
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
        User user = event.getJDA().getUserById(args.get(0));
        String reason = String.join(" ", args.subList(1, args.size()));
        if (user == null) {
            event.getChannel().sendMessage("The ID `" + args.get(0) + "` is not in my database.").queue();
            return;
        }
        if (mentionedMembers.isEmpty()) {
            if (event.getGuild().getMember(user) != null) {
                Member target = event.getGuild().getMemberById(user.getId());
                if (reason.equals("")) {
                    if (!selfMember.canInteract(Objects.requireNonNull(target))) {
                        event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                        return;

                    }
                    event.getGuild().ban(target, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
                    channel.sendMessage(String.format("Banned %s", target.getUser().getName() + "#" + target.getUser().getDiscriminator())).queue();
                } else {
                    if (!selfMember.canInteract(Objects.requireNonNull(target))) {
                        event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                        return;
                    }
                    event.getGuild().ban(target, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
                    channel.sendMessage(String.format("Banned %s for `%s`", target.getUser().getName() + "#" + target.getUser().getDiscriminator(), reason)).queue();
                }
            } else {
                if (reason.equals("")) {
                    event.getGuild().ban(user, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
                    channel.sendMessage(String.format("Banned %s", user.getName() + "#" + user.getDiscriminator())).queue();
                } else {
                    event.getGuild().ban(user, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
                    channel.sendMessage(String.format("Banned %s for `%s`", user.getName() + "#" + user.getDiscriminator(), reason)).queue();
                }
            }
        }
    }

    @Override
    public String getHelp() {
        return "Bans the specified user-id\n" + "`" + Core.PREFIX + getInvoke() + " [ID] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "banid";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"idban"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
