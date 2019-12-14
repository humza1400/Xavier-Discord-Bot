package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BanEvent extends ListenerAdapter {

    private boolean var;

    @Override
    public void onGuildBan(@Nonnull GuildBanEvent event) {
        if (AntiRaidCommand.isActive())
        {
            User user = event.getGuild().retrieveAuditLogs().type(ActionType.BAN).complete().get(0).getUser();
            String userId = user.getId();
            if (!(userId.equals("175728291460808706") || userId.equals(event.getJDA().getSelfUser().getId()) || userId.equals("464114153616048131") || userId.equals("155149108183695360") || userId.equals("650802703949234185") || userId.equals("235148962103951360"))) {
                Member member = event.getGuild().getMemberById(userId);
                try {
                    event.getGuild().ban(member, 0).reason(String.format("wizzing")).queue();
                } catch (HierarchyException | IllegalArgumentException ex)
                {}
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userAndres = event.getJDA().getUserById("620392170326851625");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")\nWhen: " + dtf.format(now)  + "\nType: Ban\nAction Taken: Banned User");
                sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nAction Taken: `Banned User`");
                sendPrivateMessage(userAndres, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `Ban`\nAction Taken: `Banned User`");
            }

        }
    }
    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }


}
