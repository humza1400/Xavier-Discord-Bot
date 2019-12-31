package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.StreamCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.commands.moderation.SetWelcomeChannelCommand;
import me.comu.exeter.commands.moderation.ToggleWelcomeCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
                Member member = event.getMember();
                event.getGuild().retrieveAuditLogs().type(ActionType.BOT_ADD).queue((auditLogEntries -> {
                    User user = auditLogEntries.get(0).getUser();
                    String id = user.getId();
                    Long idLong = user.getIdLong();
                    if (idLong.equals(Core.OWNERID) || id.equals(event.getJDA().getSelfUser().getId()) || id.equals(event.getGuild().getOwnerId()) || WhitelistCommand.getWhitelistedIDs().containsKey(id)) {
                        return;
                    }
                    event.getGuild().ban(member, 1).reason("wizz??").queue();
                    User userComu = event.getJDA().getUserById("175728291460808706");
                    User userOwner = event.getGuild().getOwner().getUser();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    Wrapper.sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot`");
                    Wrapper.sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot`");
                    if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                        for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                            if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                User whitelistUser = event.getJDA().getUserById(x);
                                if (whitelistUser != null && !whitelistUser.isBot())
                                    Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + user.getName() + "#" + user.getDiscriminator() + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot`");
                            }
                        }
                    }
                }));

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
                event.getGuild().getDefaultChannel().sendMessage(joinEmbed.build()).queue();
            else
                event.getGuild().getTextChannelById(SetWelcomeChannelCommand.logChannelID).sendMessage(joinEmbed.build()).queue();
//            try {
//                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("member", true).get(0)).queue();
//            } catch (IndexOutOfBoundsException | HierarchyException ex) {
//                event.getGuild().createRole().setName("member").setMentionable(false).setColor(0xfcba03).queue();
//                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("member", true).get(0)).queue();
//            }
        }

    }


}
