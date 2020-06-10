package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.WhitelistedJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.CompositeKey;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnwhitelistCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to unwhitelist anyone").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("You need to specify a user to unwhitelist").queue();
            return;
        }

        if (WhitelistCommand.getWhitelistedIDs().containsKey(CompositeKey.of(event.getGuild().getId(), args.get(0)))) {
            WhitelistCommand.getWhitelistedIDs().remove(CompositeKey.of(event.getGuild().getId(), args.get(0)));
            event.getChannel().sendMessage("Successfully Removed `" + args.get(0) + "` from the whitelist").queue();
            WhitelistedJSONHandler.saveWhitelistConfig();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (memberList.isEmpty()) {
            String id = args.get(0);
            try {
                Member member = event.getGuild().getMemberById(Long.parseLong(id));
                if (!Wrapper.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), id, event.getGuild().getId())) {
                    event.getChannel().sendMessage("That user is not whitelisted!").queue();
                    return;
                }
                WhitelistCommand.getWhitelistedIDs().remove(CompositeKey.of(event.getGuild().getId(), id));
                event.getChannel().sendMessage("Successfully removed `" + Objects.requireNonNull(member).getUser().getName() + "#" + member.getUser().getDiscriminator() + "` from the whitelist").queue();
                WhitelistedJSONHandler.saveWhitelistConfig();
            } catch (NumberFormatException ex) {
                String[] split = id.split("#");
                try {
                    User user = event.getJDA().getUserByTag(split[0], split[1]);
                    if (!Wrapper.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), event.getGuild().getId(), Objects.requireNonNull(user).getId())) {
                        event.getChannel().sendMessage("That user is not whitelisted!").queue();
                        return;
                    }
                    WhitelistCommand.getWhitelistedIDs().remove(CompositeKey.of(event.getGuild().getId(), user.getId()));
                    event.getChannel().sendMessage("Successfully removed `" + user.getName() + "#" + user.getDiscriminator() + "` from the whitelist").queue();
                    WhitelistedJSONHandler.saveWhitelistConfig();
                } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException exx) {
                    event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                }
            } catch (NullPointerException ex) {
                event.getChannel().sendMessage("Invalid ID + " + id.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
            }
        } else {
            String id = memberList.get(0).getId();
            if (!Wrapper.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), id, event.getGuild().getId())) {
                event.getChannel().sendMessage("That user is not whitelisted!").queue();
                return;
            }
            if (WhitelistCommand.getWhitelistedIDs().containsKey(CompositeKey.of(event.getGuild().getId(), id))) {
                WhitelistCommand.getWhitelistedIDs().remove(CompositeKey.of(event.getGuild().getId(), id));
                event.getChannel().sendMessage("Successfully removed `" + Objects.requireNonNull(event.getJDA().getUserById(id)).getName() + "#" + Objects.requireNonNull(event.getJDA().getUserById(id)).getDiscriminator() + "` from the whitelist hash").queue();
                WhitelistedJSONHandler.saveWhitelistConfig();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Unwhitelists the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unwhitelist";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"removewhitelist", "uwl", "untrust", "unwl"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
