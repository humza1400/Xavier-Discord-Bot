package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class BanEvent extends ListenerAdapter {

    @Override
    public void onGuildBan(@Nonnull GuildBanEvent event) {
        if (AntiRaidCommand.isActive())
        {
            event.getGuild().retrieveAuditLogs().type(ActionType.BAN).queue((auditLogEntries -> {
                User user = auditLogEntries.get(0).getUser();
                String userId = user.getId();
                if (user.getIdLong() != Core.OWNERID && !userId.equals(event.getJDA().getSelfUser().getId()) && !userId.equals(event.getGuild().getOwnerId()) && !userId.equals("464114153616048131") && !userId.equals("155149108183695360") && !userId.equals("650802703949234185") && !userId.equals("235148962103951360") && !WhitelistCommand.getWhitelistedIDs().containsKey(userId)) {
                    Member member = event.getGuild().getMemberById(userId);
                    try {
                        event.getGuild().ban(member, 0).reason(String.format("wizzing")).queue();
                    } catch (HierarchyException | IllegalArgumentException ex)
                    {}
                    User userComu = event.getJDA().getUserById("175728291460808706");
                    User userOwner = event.getGuild().getOwner().getUser();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nAction Taken: `Banned User`");
                    sendPrivateMessage(userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nAction Taken: `Banned User`");
                    if (!WhitelistCommand.getWhitelistedIDs().isEmpty())
                    {
                        for (String x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                            if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                User whitelistUser = event.getJDA().getUserById(x);
                                if (!whitelistUser.isBot())
                                    Wrapper.sendPrivateMessage(event.getJDA().getUserById(x), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nAction Taken: `Banned User`");
                            }
                        }
                    }
                }
            }));

        }
    }
    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

}
