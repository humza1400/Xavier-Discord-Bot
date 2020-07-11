package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.util.CompositeKey;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class BanEvent extends ListenerAdapter {

    @Override
    public void onGuildBan(@Nonnull GuildBanEvent event) {
        if (AntiRaidCommand.isActive() && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getGuild().retrieveAuditLogs().type(ActionType.BAN).queue((auditLogEntries -> {
                User user = auditLogEntries.get(0).getUser();
                String userId = Objects.requireNonNull(user).getId();
                if (Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), userId, event.getGuild().getId())) {
                    int permissionLevel = Integer.parseInt(WhitelistCommand.getWhitelistedIDs().get(CompositeKey.of(event.getGuild().getId(), userId)));
                    if (permissionLevel == 0)
                        return;
                }
                if (user.getIdLong() != Core.OWNERID && !userId.equals(event.getJDA().getSelfUser().getId()) && !userId.equals(event.getGuild().getOwnerId())) {
                    Member member = event.getGuild().getMemberById(userId);
                    if (member != null && !event.getGuild().getSelfMember().canInteract(member))
                        return;
                    event.getGuild().ban(Objects.requireNonNull(member), 0).reason("Triggered Anti-Nuke").queue();
                    String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                    String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String botCheck = Objects.requireNonNull(member).getUser().isBot() ? "`Yes`" : "`No`";
                    Utility.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + MarkdownUtil.monospace(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")") + "\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nBot: " + botCheck + "\nAction Taken: `Banned User`");
                    Utility.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + MarkdownUtil.monospace(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")") + "`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nBot: " + botCheck + "\nAction Taken: `Banned User`");
                    if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                        for (CompositeKey x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                            if (Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), x.getUserID(), x.getGuildID()) && x.getGuildID().equals(event.getGuild().getId())) {
                                User whitelistUser = event.getJDA().getUserById(x.getUserID());
                                if (!Objects.requireNonNull(whitelistUser).isBot())
                                    Utility.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x.getUserID())).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + MarkdownUtil.monospace(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")") + "`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nBot: " + botCheck + "\nAction Taken: `Banned User`");
                            }
                        }
                    }
                }
            }));
        }
    }
}
