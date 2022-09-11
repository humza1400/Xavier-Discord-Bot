package me.comu.exeter.events;

import me.comu.exeter.commands.admin.BlacklistCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.commands.misc.WelcomePingCommand;
import me.comu.exeter.commands.moderation.MuteCommand;
import me.comu.exeter.commands.moderation.SetMuteRoleCommand;
import me.comu.exeter.commands.moderation.SetWelcomeChannelCommand;
import me.comu.exeter.commands.moderation.ToggleWelcomeCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.objects.ObjectKey;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.util.Captcha;
import me.comu.exeter.utility.Utility;
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
        Core.getInstance().getJDA().getPresence().setActivity(Activity.competing(String.format("%s", amount)));
        if (Utility.isAntiRaidEnabled(event.getGuild().getId()) && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (event.getMember().getUser().isBot()) {
                Member member = event.getMember();
                event.getGuild().retrieveAuditLogs().type(ActionType.BOT_ADD).queue((auditLogEntries -> {
                    User user = auditLogEntries.get(0).getUser();
                    String id = Objects.requireNonNull(user).getId();
                    Long idLong = user.getIdLong();
                    if (Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), id, event.getGuild().getId())) {
                        int permissionLevel = Integer.parseInt(WhitelistCommand.getWhitelistedIDs().get(WhitelistKey.of(event.getGuild().getId(), id)));
                        if (permissionLevel == 0)
                            return;
                    }
                    if (idLong.equals(Core.OWNERID) || id.equals(event.getJDA().getSelfUser().getId()) || id.equals(event.getGuild().getOwnerId())) {
                        return;
                    }
                    event.getGuild().ban(member, 1).reason("Triggered Anti-Nuke").queue();
                    Member humanMember = event.getGuild().getMemberById(id);
                    List<Role> roles = Objects.requireNonNull(humanMember).getRoles();
                    String[] stringArray = new String[humanMember.getRoles().size()];
                    List<String> strings = Arrays.asList(stringArray);
                    for (int i = 0; i < roles.size(); i++) {
                        stringArray[i] = roles.get(i).getName();
                    }
                    stringArray = strings.toArray(new String[0]);
                    for (Role role : humanMember.getRoles()) {
                        if (event.getGuild().getSelfMember().canInteract(role) && role.isManaged()) {
                            role.getManager().revokePermissions(Permission.values()).queue();
                        }
                        if (event.getGuild().getSelfMember().canInteract(role) && !role.isManaged()) {
                            event.getGuild().removeRoleFromMember(humanMember.getId(), role).queue();
                        }
                    }
                    String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                    String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    Utility.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + Utility.removeMarkdown(event.getGuild().getName()) + "**\nWizzer: `" + Utility.removeMarkdown(user.getAsTag()) + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot & Removed Roles: \n" + Arrays.deepToString(stringArray) + "`");
                    Utility.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + Utility.removeMarkdown(event.getGuild().getName()) + "**\nWizzer: `" + Utility.removeMarkdown(user.getAsTag()) + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot & Removed Roles: \n" + Arrays.deepToString(stringArray) + "`");
                    if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                        for (WhitelistKey x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                            if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                User whitelistUser = event.getJDA().getUserById(x.getUserID());
                                if (whitelistUser != null && !whitelistUser.isBot())
                                    Utility.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x.getUserID())).getId(), "**Anti-Raid Report For " + Utility.removeMarkdown(event.getGuild().getName()) + "**\nWizzer: `" + Utility.removeMarkdown(user.getAsTag()) + " (" + user.getId() + ")`" + "\nBot: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Added Bot`\nAction Taken: `Banned Bot & Removed Roles: \n" + Arrays.deepToString(stringArray) + "`");
                            }
                        }
                    }
                }));

            }
        }
        for (String x : BlacklistCommand.getBlacklistedUsers().keySet()) {
            if (x.equals(event.getMember().getId()) && BlacklistCommand.getBlacklistedUsers().get(x).equals(event.getGuild().getId())) {
                event.getGuild().ban(event.getMember(), 0, "Blacklisted").queue();
            }
        }
        if (Captcha.captchaEnabledServers.containsKey(event.getGuild().getId())) {
            if (Captcha.captchaEnabledServers.get(event.getGuild().getId()) != null) {
                Role role = event.getJDA().getRoleById(Captcha.captchaEnabledServers.get(event.getGuild().getId()));
                if (role != null && !event.getMember().getRoles().contains(role)) {
                    String captcha = Captcha.generateText();
                    byte[] captchaBytes = Captcha.generateImage(captcha);
                    event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle("**Welcome to " + event.getGuild().getName() + "**");
                        embed.setDescription("Please send the captcha code here.\n\nHello! You are required to complete a captcha before entering the server.\n**NOTE:** This is **Case Sensitive**.\n\n**Why?**\nThis is to protect the server against\ntargeted attacks using automated user accounts.\n\n**Your Captcha:**");
                        embed.setImage("attachment://captcha.png");
                        privateChannel.sendFile(captchaBytes, "captcha.png").setEmbeds(embed.build()).queue(null, failure -> Logger.getLogger().print("Unable to DM and verify " + event.getMember().getUser().getAsTag()));
                        Captcha.captchaUsers.put(WhitelistKey.of(event.getGuild().getId(), event.getUser().getId()), ObjectKey.of(captcha, 3));
                    });
                }
            }
        }

        if (!event.getMember().getUser().isBot() && WelcomePingCommand.welcomePingChannels.containsKey(event.getGuild().getId())) {
            List<String> channels = WelcomePingCommand.welcomePingChannels.get(event.getGuild().getId());
            if (channels.isEmpty()) {
                WelcomePingCommand.welcomePingChannels.remove(event.getGuild().getId());
            } else {
                for (String channel : channels) {
                    TextChannel textChannel = event.getJDA().getTextChannelById(channel);
                    if (textChannel == null) {
                        channels.remove(channel);
                    } else {
                        textChannel.sendMessage(event.getMember().getAsMention()).queue(success -> success.delete().queue());
                    }
                }
            }
        }

        if (SetMuteRoleCommand.getMutedRoleMap().containsKey(event.getGuild().getId()) && MuteCommand.mutedUsers.contains(event.getMember().getId())) {
            Role role = event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId()));
            if (role != null)
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
        }
        if (ToggleWelcomeCommand.isActive()) {
            Random random = new Random();
            int number = random.nextInt(messages.length);
            EmbedBuilder joinEmbed = new EmbedBuilder();
            joinEmbed.setColor(0x66d8ff);
            if (event.getMember().getUser().isBot())
                joinEmbed.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()) + " **(BOT)**");
            else
                joinEmbed.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));
            if (!SetWelcomeChannelCommand.bound)
                Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessageEmbeds(joinEmbed.build()).queue();
            else
                Objects.requireNonNull(event.getGuild().getTextChannelById(SetWelcomeChannelCommand.logChannelID)).sendMessageEmbeds(joinEmbed.build()).queue();
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
        WhitelistCommand.getGuilds().put(event.getGuild().getId(), false);
        String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
        try {
            Utility.sendPrivateMessage(event.getJDA(), userComu, "I was added to `" + event.getGuild().getName() + "` (" + event.getGuild().getId() + ") | discord.gg/" + event.getGuild().getTextChannels().get(0).createInvite().setMaxAge(0).complete().getCode() + " `" + Utility.removeMentionsAndMarkdown(Objects.requireNonNull(event.getGuild().getOwner()).getUser().getAsTag()) + "`");
        } catch (Exception ex) {
            Utility.sendPrivateMessage(event.getJDA(), userComu, "I was added to `" + event.getGuild().getName() + "` (" + event.getGuild().getId() + ") | Couldn't resolve an invite" + " `" + Utility.removeMentionsAndMarkdown(Objects.requireNonNull(event.getGuild().getOwner()).getUser().getAsTag()) + "`");
        }

    }

/*    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        event.getChannel().sendMessage(event.getMember().getAsMention() + " removed **:" + event.getReactionEmote().getName() + ":** for https://discordapp.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessageId()).queue();
    }*/
}
