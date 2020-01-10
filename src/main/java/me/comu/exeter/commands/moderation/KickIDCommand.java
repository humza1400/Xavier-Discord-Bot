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

public class KickIDCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!member.hasPermission(Permission.KICK_MEMBERS) && member.getIdLong() != Core.OWNERID/* || !member.canInteract(target)*/) {
            channel.sendMessage("You don't have permission to kick that user").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I don't have permissions to kick users").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Please specify a valid user to kick").queue();
            return;
        }
        User user = event.getJDA().getUserById(args.get(0));
        String reason = String.join(" ", args.subList(1, args.size()));
        if (user == null) {
            event.getChannel().sendMessage("Couldn't find the user with the given tag of `" + args.get(0) + "`").queue();
            return;
        }
        if (mentionedMembers.isEmpty()) {
            Member target = event.getGuild().getMemberById(user.getId());
            if (reason.equals("")) {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessage("My role is not high enough to kick that user!").queue();
                    return;

                }
                event.getGuild().kick(target).reason(String.format("Kicked by %#s", event.getAuthor())).queue();
                channel.sendMessage(String.format("Kicked %s", target.getUser().getName() + "#" + target.getUser().getDiscriminator())).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Kicks the specified user-ID\n" + "`" + Core.PREFIX + getInvoke() + " [ID] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kickid";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"idkick"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
