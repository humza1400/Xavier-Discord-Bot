package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class BlacklistCommand implements ICommand {

    private static HashMap<String, String> blacklistedUsers = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to blacklist anyone.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to blacklist anyone.").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to blacklist.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear")) {
            if (blacklistedUsers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Nobody is blacklisted.").build()).queue();
                return;
            }
            blacklistedUsers.clear();
            Core.getInstance().saveConfig(Core.getInstance().getBlacklistedJSONHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed("Purged all blacklisted users.").build()).queue();
            return;
        }
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            if (!(event.getGuild().getSelfMember().canInteract(event.getMessage().getMentionedMembers().get(0)))) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You cannot blacklist that user.").build()).queue();
                return;
            }
            blacklistedUsers.put(event.getMessage().getMentionedMembers().get(0).getId(), event.getGuild().getId());
            Core.getInstance().saveConfig(Core.getInstance().getBlacklistedJSONHandler());
            event.getGuild().ban(event.getMessage().getMentionedMembers().get(0).getUser(), 0, "Blacklisted").queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Blacklisted " + event.getMessage().getMentionedMembers().get(0).getAsMention()).build()).queue();
        } else {
            try {
                event.getJDA().retrieveUserById(args.get(0)).queue(user -> {
                    if (user == null) {
                        blacklistedUsers.put(args.get(0), event.getGuild().getId());
                        Core.getInstance().saveConfig(Core.getInstance().getBlacklistedJSONHandler());
                        event.getChannel().sendMessage("Blacklisted **" + Utility.removeMentions(args.get(0)) + "**.\nNote: That user wasn't in my cache, but if they try joining they'll be unable to.").queue();
                        return;
                    }
                    blacklistedUsers.put(user.getId(), event.getGuild().getId());
                    Core.getInstance().saveConfig(Core.getInstance().getBlacklistedJSONHandler());
                    event.getGuild().ban(user, 0, "Blacklisted").queue();
                    event.getChannel().sendMessage("Blacklisted " + user.getAsTag()).queue();
                });
            } catch (NullPointerException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid ID.").build()).queue();
            }
        }

    }

    public static HashMap<String, String> getBlacklistedUsers() {
        return blacklistedUsers;
    }

    public static void setBlacklistedUsers(Map<String, String> map) {
        blacklistedUsers.clear();
        blacklistedUsers.putAll(map);
    }

    @Override
    public String getHelp() {
        return "Blacklists the specified user from the guild\n`" + Core.PREFIX + getInvoke() + " [user]/[clear]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "blacklist";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bl"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
