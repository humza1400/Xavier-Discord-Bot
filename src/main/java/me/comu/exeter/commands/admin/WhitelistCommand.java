package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WhitelistCommand implements ICommand {

    private static HashMap<String, String> whitelistedIDs = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong() && !event.getMember().getId().equalsIgnoreCase("210956619788320768")) {
            event.getChannel().sendMessage("You don't have permission to whitelist anyone").queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("You need to specify a user to whitelist").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("-g"))
        {
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        String guildID = event.getGuild().getId();
        String guildOwnerId = event.getGuild().getOwnerId();
        if (mentionedMembers.isEmpty()) {
            String id = args.get(0);
            try {
                User userID = event.getJDA().getUserById(id);
                Member member = event.getGuild().getMemberById(Long.parseLong(id));
                if (whitelistedIDs.containsKey(id) && whitelistedIDs.containsValue(guildID))
                {
                    event.getChannel().sendMessage("User already whitelisted.").queue();
                    return;
                }
                if (Objects.requireNonNull(userID).getIdLong() == Core.OWNERID || userID.getId().equals(guildOwnerId))
                {
                    event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                    return;
                }
                whitelistedIDs.put(id, guildID);
                event.getChannel().sendMessage("Added `" + Objects.requireNonNull(member).getUser().getName() + "#" + member.getUser().getDiscriminator() + "` to the whitelist").queue();
                WhitelistedJSONHandler.saveWhitelistConfig();
            } catch (NumberFormatException ex) {
                String[] split = id.split("#");
                try {
                    User user = event.getJDA().getUserByTag(split[0], split[1]);
                    if (WhitelistCommand.getWhitelistedIDs().containsKey(Objects.requireNonNull(user).getId()))
                    {
                        event.getChannel().sendMessage("That user is already whitelisted!").queue();
                        return;
                    }
                    if (user.getIdLong() == Core.OWNERID || user.getId().equals(guildOwnerId))
                    {
                        event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                        return;
                    }
                    WhitelistCommand.getWhitelistedIDs().put(user.getId(), guildID);
                    event.getChannel().sendMessage("Successfully added `" + user.getName() + "#" + user.getDiscriminator() + "` to the whitelist").queue();
                } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException exx)
                {
                    event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone","everyone".replaceAll("@here","here"))).queue();
                }
            } catch (NullPointerException ex)
            {
                event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone","everyone".replaceAll("@here","here"))).queue();
            }
        } else {
            if (whitelistedIDs.containsKey(mentionedMembers.get(0).getId()) && whitelistedIDs.containsValue(guildID))
            {
                event.getChannel().sendMessage("User already whitelisted.").queue();
                return;
            }
            if (mentionedMembers.get(0).getIdLong() == Core.OWNERID || mentionedMembers.get(0).getId().equals(guildOwnerId))
            {
                event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                return;
            }
            whitelistedIDs.put(mentionedMembers.get(0).getId(), guildID);
            event.getChannel().sendMessage("Added `" + mentionedMembers.get(0).getUser().getName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator() + "` to the whitelist hash").queue();
            WhitelistedJSONHandler.saveWhitelistConfig();
        }

    }

    public static HashMap<String, String> getWhitelistedIDs() {
        return whitelistedIDs;
    }

    public static void setWhitelistedIDs(HashMap<String, String> whitelistedIDs) {
        WhitelistCommand.whitelistedIDs = whitelistedIDs;
    }

    @Override
    public String getHelp() {
        return "Whitelists the specific user to the bot's anti-raid features\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\n**NOTE**: Whitelisting a user will completely exclude them from anti-nuke detections, be weary on who you whitelist.";
    }

    @Override
    public String getInvoke() {
        return "whitelist";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"arwhitelist","artrust","antiraidtrust","antiraidwhitelist","wl","trust"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
