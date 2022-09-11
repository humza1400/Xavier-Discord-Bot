package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.utility.Utility;
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
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to un-whitelist anyone.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to specify a user to un-whitelist.").build()).queue();
            return;
        }

        if (WhitelistCommand.getWhitelistedIDs().containsKey(WhitelistKey.of(event.getGuild().getId(), args.get(0)))) {
            WhitelistCommand.getWhitelistedIDs().remove(WhitelistKey.of(event.getGuild().getId(), args.get(0)));
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully Removed `" + args.get(0) + "` from the whitelist.").build()).queue();
            Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (memberList.isEmpty()) {
            String id = args.get(0);
            try {
                Member member = event.getGuild().getMemberById(Long.parseLong(id));
                if (!Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), id, event.getGuild().getId())) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not whitelisted!").build()).queue();
                    return;
                }
                WhitelistCommand.getWhitelistedIDs().remove(WhitelistKey.of(event.getGuild().getId(), id));
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully removed `" + Objects.requireNonNull(member).getUser().getName() + "#" + member.getUser().getDiscriminator() + "` from the whitelist.").build()).queue();
                Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
            } catch (NumberFormatException ex) {
                String[] split = id.split("#");
                try {
                    User user = event.getJDA().getUserByTag(split[0], split[1]);
                    if (!Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), event.getGuild().getId(), Objects.requireNonNull(user).getId())) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not whitelisted!").build()).queue();
                        return;
                    }
                    WhitelistCommand.getWhitelistedIDs().remove(WhitelistKey.of(event.getGuild().getId(), user.getId()));
                    event.getChannel().sendMessageEmbeds(Utility.embed("Successfully removed `" + user.getName() + "#" + user.getDiscriminator() + "` from the whitelist.").build()).queue();
                    Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
                } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException exx) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid ID + " + Utility.removeMentions(id) + ".").build()).queue();
                }
            } catch (NullPointerException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid ID + " + Utility.removeMentions(id) + ".").build()).queue();
            }
        } else {
            String id = memberList.get(0).getId();
            if (!Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), id, event.getGuild().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not whitelisted!").build()).queue();
                return;
            }
            if (WhitelistCommand.getWhitelistedIDs().containsKey(WhitelistKey.of(event.getGuild().getId(), id))) {
                WhitelistCommand.getWhitelistedIDs().remove(WhitelistKey.of(event.getGuild().getId(), id));
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully removed `" + Objects.requireNonNull(event.getJDA().getUserById(id)).getName() + "#" + Objects.requireNonNull(event.getJDA().getUserById(id)).getDiscriminator() + "` from the whitelist hash.").build()).queue();
                Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
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

    @Override
    public boolean isPremium() {
        return true;
    }
}
