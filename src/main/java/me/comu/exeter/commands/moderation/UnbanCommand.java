package me.comu.exeter.commands.moderation;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnbanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();

        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS) && event.getMember().getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to unban someone").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have permissions to unban someone").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Please specify a user to unban").queue();
            return;
        }

        String argsJoined = String.join(" ", args);

        event.getGuild().retrieveBanList().queue((bans) -> {

            List<User> goodUsers = bans.stream().filter((ban) -> isCorrectUser(ban, argsJoined)).map(Guild.Ban::getUser).collect(Collectors.toList());

            if (goodUsers.isEmpty()) {
                channel.sendMessage("That user is not banned").queue();
                return;
            }

            User target = goodUsers.get(0);

            String mod = String.format("%#s", event.getAuthor());
            String bannedUser = String.format("%#s", target);

            event.getGuild().unban(target).reason("Unbanned By " + mod).queue();

            channel.sendMessage("Unbanned " + bannedUser).queue();

        });
    }

    @Override
    public String getHelp() {
        return "Unbans the specific user from the server\n" +
                "`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unban";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"pardon"};
    }

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();
        return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg) || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
    }
}