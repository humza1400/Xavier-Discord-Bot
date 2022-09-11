package me.comu.exeter.commands.moderation;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnbanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to unban someone").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to unban someone").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to unban").build()).queue();
            return;
        }

        String argsJoined = String.join(" ", args);

        event.getGuild().retrieveBanList().queue((bans) -> {

            List<User> goodUsers = bans.stream().filter((ban) -> isCorrectUser(ban, argsJoined)).map(Guild.Ban::getUser).collect(Collectors.toList());

            if (goodUsers.isEmpty()) {
                List<User> fans = bans.stream().map(Guild.Ban::getUser).collect(Collectors.toList());
                User member1 = Utility.findSimilarUser(args.get(0), fans);
                if (member1 != null) {
                    event.getGuild().unban(member1).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed("Unbanned " + Utility.removeMarkdown(member1.getAsTag())).build()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(Utility.embed("That user is not banned").build()).queue();
                return;
            }

            User target = goodUsers.get(0);

            String mod = String.format("%#s", event.getAuthor());
            String bannedUser = String.format("%#s", target);

            event.getGuild().unban(target).reason("Unbanned by " + mod).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Unbanned " + bannedUser).build()).queue();

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
        return new String[]{"pardon"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return false;
    }

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User bannedUser = ban.getUser();
        return bannedUser.getName().equalsIgnoreCase(arg) || bannedUser.getId().equals(arg) || String.format("%#s", bannedUser).equalsIgnoreCase(arg);
    }
}