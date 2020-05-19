package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class BanEvent extends ListenerAdapter {

    @Override
    public void onGuildBan(@Nonnull GuildBanEvent event) {
        if (AntiRaidCommand.isActive() && event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR))
        {
            event.getGuild().retrieveAuditLogs().type(ActionType.BAN).queue((auditLogEntries -> {
                User user = auditLogEntries.get(0).getUser();
                String userId = Objects.requireNonNull(user).getId();
                if (user.getIdLong() != Core.OWNERID && !userId.equals(event.getJDA().getSelfUser().getId()) && !userId.equals(event.getGuild().getOwnerId()) && !userId.equals("464114153616048131") && !userId.equals("155149108183695360") && !userId.equals("650802703949234185") && !userId.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(userId)) {
                    Member member = event.getGuild().getMemberById(userId);
                    try {
                        event.getGuild().ban(Objects.requireNonNull(member), 0).reason("wizzing").queue();
                    } catch (HierarchyException | IllegalArgumentException ignored)
                    {}
                    String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                    String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    String botCheck = Objects.requireNonNull(member).getUser().isBot() ? "`Yes`" : "`No`";
                    Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + MarkdownUtil.monospace(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")")+"\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nBot: " + botCheck + "\nAction Taken: `Banned User`");
                    Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + MarkdownUtil.monospace(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")")+"`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nBot: " + botCheck + "\nAction Taken: `Banned User`");
                    if (!WhitelistCommand.getWhitelistedIDs().isEmpty())
                    {
                        for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                            if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                User whitelistUser = event.getJDA().getUserById(x);
                                if (!Objects.requireNonNull(whitelistUser).isBot())
                                    Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x)).getId(),"**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + MarkdownUtil.monospace(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")")+"`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nBot: " + botCheck + "\nAction Taken: `Banned User`");
                            }
                        }
                    }
                }
            }));
        }
    }
}
