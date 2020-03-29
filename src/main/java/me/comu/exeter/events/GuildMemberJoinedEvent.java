package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.BlacklistCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.commands.moderation.SetWelcomeChannelCommand;
import me.comu.exeter.commands.moderation.ToggleWelcomeCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GuildMemberJoinedEvent extends ListenerAdapter {
    private static final String[] messages = {"[member] joined", "welcome [member]", "hello [member]", "[member] has arrived", "we've been missing you [member]", "[member] is amazing for joining", "Yo [member]", "everyone welcome [member]", "what it do [member]"};

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        int amount = 0;
        for (Guild g : event.getJDA().getGuilds()) {
            amount += g.getMembers().size();
        }
        Core.jda.getPresence().setActivity(Activity.watching(String.format("over %s users", amount)));

        if (AntiRaidCommand.isActive()) {
            if (event.getMember().getUser().isBot()) {
                if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR))
                {
                    String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                    Wrapper.sendPrivateMessage(event.getJDA(), userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_BOT_ADD**");
                    return;
                }
                Member member = event.getMember();
                event.getGuild().retrieveAuditLogs().type(ActionType.BOT_ADD).queue((auditLogEntries -> {
                    User user = auditLogEntries.get(0).getUser();
                    String id = Objects.requireNonNull(user).getId();
                    Long idLong = user.getIdLong();
                    if (idLong.equals(Core.OWNERID) || id.equals(event.getJDA().getSelfUser().getId()) || id.equals(event.getGuild().getOwnerId()) || WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        return;
                    }
                    event.getGuild().ban(member, 1).reason("wizz??").queue();
                    Member humanMember = event.getGuild().getMemberById(id);
                    List<Role> roles = Objects.requireNonNull(humanMember).getRoles();
                    String[] stringArray = new String[humanMember.getRoles().size()];
                    List<String> strings = Arrays.asList(stringArray);
                    for (int i = 0; i < roles.size(); i++) {
                        stringArray[i] = roles.get(i).getName();
                    }
                    stringArray = strings.toArray(new String[0]);
                    for (Role role : humanMember.getRoles()) {
                        if (role.isManaged()) { // for safe keeping
                            role.getManager().revokePermissions(Permission.values()).queue();
                        }
                        if (!role.isManaged()) {
                            event.getGuild().removeRoleFromMember(humanMember.getId(), role).queue();
                        }
                    }
                    String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                    String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    Wrapper.sendPrivateMessage(event.getJDA(),userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot & Removed Roles: \n" + Arrays.deepToString(stringArray) + "`");
                    Wrapper.sendPrivateMessage(event.getJDA(),userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot & Removed Roles: \n" + Arrays.deepToString(stringArray) + "`");
                    if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                        for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                            if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                User whitelistUser = event.getJDA().getUserById(x);
                                if (whitelistUser != null && !whitelistUser.isBot())
                                    Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot & Removed Roles: \n" + Arrays.deepToString(stringArray) + "`");
                            }
                        }
                    }
                }));

            }
        }
        for (String x : BlacklistCommand.blacklistedUsers.keySet()) {
            if (BlacklistCommand.blacklistedUsers.get(x).equals(event.getGuild().getId())) {
            event.getGuild().ban(event.getMember(), 0, "Blacklisted").queue();
            }
        }
        if (ToggleWelcomeCommand.isActive()) {
            Random random = new Random();
            int number = random.nextInt(messages.length);
            EmbedBuilder joinEmbed = new EmbedBuilder();
            joinEmbed.setColor(0x66d8ff);
            if (event.getMember().getUser().isBot())
                joinEmbed.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()) + " (BOT)");
            else
                joinEmbed.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));
            if (!SetWelcomeChannelCommand.bound)
                Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(joinEmbed.build()).queue();
            else
                Objects.requireNonNull(event.getGuild().getTextChannelById(SetWelcomeChannelCommand.logChannelID)).sendMessage(joinEmbed.build()).queue();
//            try {
//                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("member", true).get(0)).queue();
//            } catch (IndexOutOfBoundsException | HierarchyException ex) {
//                event.getGuild().createRole().setName("member").setMentionable(false).setColor(0xfcba03).queue();
//                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("member", true).get(0)).queue();
//            }
        }


    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
        try {
            Wrapper.sendPrivateMessage(event.getJDA(), userComu, "I was added to `" + event.getGuild().getName() + "` (" + event.getGuild().getId() + ") | discord.gg/" + event.getGuild().getTextChannels().get(0).createInvite().setMaxAge(0).complete().getCode());
        } catch (Exception ex)
        {
            Wrapper.sendPrivateMessage(event.getJDA(), userComu, "I was added to `" + event.getGuild().getName() + "` (" + event.getGuild().getId() + ") | Couldn't resolve an invite");
        }


    }

/*    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        event.getChannel().sendMessage(event.getMember().getAsMention() + " removed **:" + event.getReactionEmote().getName() + ":** for https://discordapp.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessageId()).queue();
    }*/
}
