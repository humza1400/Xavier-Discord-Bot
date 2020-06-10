package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.WhitelistedJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.CompositeKey;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class WhitelistCommand implements ICommand {

    private static final Map<CompositeKey, String> whitelistedIDs = Collections.checkedMap(new HashMap<>(), CompositeKey.class, String.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to whitelist anyone").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("You need to specify a user to whitelist").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("-g")) {
            return;
        }
        if (args.size() == 2) {
            try {
                int permissionLevel = Integer.parseInt(args.get(1));
                if (permissionLevel < 0 || permissionLevel > 3) {
                    event.getChannel().sendMessage("Invalid Permission-Level Index (0-3), try again `" + Core.PREFIX + getInvoke() + "`").queue();
                    return;
                }
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessage("Invalid Permission-Level, try again `" + Core.PREFIX + getInvoke() + "`").queue();
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
                    if (whitelistedIDs.keySet().contains(CompositeKey.of(guildID, id))) {
                        event.getChannel().sendMessage("User already whitelisted.").queue();
                        return;
                    }
                    if (Objects.requireNonNull(userID).getIdLong() == Core.OWNERID || userID.getId().equals(guildOwnerId)) {
                        event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                        return;
                    }
                    if (Objects.requireNonNull(userID).getIdLong() == event.getGuild().getSelfMember().getIdLong()|| userID.getId().equals(guildOwnerId)) {
                        event.getChannel().sendMessage("The bot is automatically whitelisted.").queue();
                        return;
                    }
                    whitelistedIDs.put(CompositeKey.of(guildID, id), args.get(1));
                    event.getChannel().sendMessage("Added `" + Objects.requireNonNull(member).getUser().getAsTag().replaceAll("([_`~*>])", "\\\\$1") + "` to the whitelist with level `" + args.get(1) + "`").queue();
                    WhitelistedJSONHandler.saveWhitelistConfig();
                } catch (NumberFormatException ex) {
                    String[] split = id.split("#");
                    try {
                        User user = Objects.requireNonNull(event.getJDA().getUserByTag(split[0], split[1]));
                        if (whitelistedIDs.keySet().contains(CompositeKey.of(guildID, user.getId()))) {
                            event.getChannel().sendMessage("That user is already whitelisted!").queue();
                            return;
                        }
                        if (user.getIdLong() == Core.OWNERID || user.getId().equals(guildOwnerId)) {
                            event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                            return;
                        }
                        WhitelistCommand.getWhitelistedIDs().put(CompositeKey.of(guildID, user.getId()), args.get(1));
                        event.getChannel().sendMessage("Successfully added `" + user.getName() + "#" + user.getDiscriminator() + "` to the whitelist with level `" + args.get(1) + "`").queue();
                    } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException exx) {
                        event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
                    }
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
                }
            } else {
                if (Wrapper.isWhitelisted(whitelistedIDs, mentionedMembers.get(0).getId(), guildID)) {
                    event.getChannel().sendMessage("User already whitelisted.").queue();
                    return;
                }
                if (mentionedMembers.get(0).getIdLong() == Core.OWNERID || mentionedMembers.get(0).getId().equals(guildOwnerId)) {
                    event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                    return;
                }
                whitelistedIDs.put(CompositeKey.of(guildID, mentionedMembers.get(0).getId()), args.get(1));
                event.getChannel().sendMessage("Added `" + mentionedMembers.get(0).getUser().getName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator() + "` to the whitelist hash with level `" + args.get(1) + "`").queue();
                WhitelistedJSONHandler.saveWhitelistConfig();
            }

        } else {
            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
            String guildID = event.getGuild().getId();
            String guildOwnerId = event.getGuild().getOwnerId();
            if (mentionedMembers.isEmpty()) {
                String id = args.get(0);
                try {
                    User userID = event.getJDA().getUserById(id);
                    Member member = event.getGuild().getMemberById(Long.parseLong(id));
                    if (whitelistedIDs.keySet().contains(CompositeKey.of(guildID, id))) {
                        event.getChannel().sendMessage("User already whitelisted.").queue();
                        return;
                    }
                    if (Objects.requireNonNull(userID).getIdLong() == Core.OWNERID || userID.getId().equals(guildOwnerId)) {
                        event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                        return;
                    }
                    whitelistedIDs.put(CompositeKey.of(guildID, id), "0");
                    event.getChannel().sendMessage("Added `" + Objects.requireNonNull(member).getUser().getName() + "#" + member.getUser().getDiscriminator() + "` to the whitelist with level `0`").queue();
                    WhitelistedJSONHandler.saveWhitelistConfig();
                } catch (NumberFormatException ex) {
                    String[] split = id.split("#");
                    try {
                        User user = Objects.requireNonNull(event.getJDA().getUserByTag(split[0], split[1]));
                        if (whitelistedIDs.keySet().contains(CompositeKey.of(guildID, user.getId()))) {
                            event.getChannel().sendMessage("That user is already whitelisted!").queue();
                            return;
                        }
                        if (user.getIdLong() == Core.OWNERID || user.getId().equals(guildOwnerId)) {
                            event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                            return;
                        }
                        WhitelistCommand.getWhitelistedIDs().put(CompositeKey.of(guildID, user.getId()), "0");
                        event.getChannel().sendMessage("Successfully added `" + user.getName() + "#" + user.getDiscriminator() + "` to the whitelist with level `0`").queue();
                    } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException exx) {
                        event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
                    }
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
                }
            } else {
                if (Wrapper.isWhitelisted(whitelistedIDs, mentionedMembers.get(0).getId(), guildID)) {
                    event.getChannel().sendMessage("User already whitelisted.").queue();
                    return;
                }
                if (mentionedMembers.get(0).getIdLong() == Core.OWNERID || mentionedMembers.get(0).getId().equals(guildOwnerId)) {
                    event.getChannel().sendMessage("The owner is automatically whitelisted.").queue();
                    return;
                }
                whitelistedIDs.put(CompositeKey.of(guildID, mentionedMembers.get(0).getId()), "0");
                event.getChannel().sendMessage("Added `" + mentionedMembers.get(0).getUser().getName() + "#" + mentionedMembers.get(0).getUser().getDiscriminator() + "` to the whitelist hash with level `0`").queue();
                WhitelistedJSONHandler.saveWhitelistConfig();
            }
        }
    }

    public static Map<CompositeKey, String> getWhitelistedIDs() {
        return whitelistedIDs;
    }

    public static void setWhitelistedIDs(Map<String, Map<String, String>> map) {
        whitelistedIDs.clear();
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            for (Map.Entry<String, String> entry1 : entry.getValue().entrySet()) {
                whitelistedIDs.put(CompositeKey.of(entry.getKey(), entry1.getKey()), entry1.getValue());
            }
        }

    }


    @Override
    public String getHelp() {
        return "Whitelists the specific user to the bot's anti-raid features\n`" + Core.PREFIX + getInvoke() + " [user] <level> (defaults 0)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\n__0__ - **ADMIN**, __1__ - **CHANNELS & ROLES**, __2__ - **CHANNELS**, __3__ - **ROLES**\n**NOTE**: Whitelisting a user will completely exclude them from anti-nuke detections, be weary on who you whitelist.";
    }

    @Override
    public String getInvoke() {
        return "whitelist";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"arwhitelist", "artrust", "antiraidtrust", "antiraidwhitelist", "wl", "trust"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
