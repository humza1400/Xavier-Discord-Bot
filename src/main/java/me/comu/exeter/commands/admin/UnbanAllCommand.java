package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnbanAllCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        if (!Objects.requireNonNull(member).hasPermission(Permission.ADMINISTRATOR) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to unban users.").build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to unban users.").build()).queue();
            return;
        }

        event.getGuild().retrieveBanList().queue((entries) -> {
            if (entries.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.embed("There are no users currently banned.").build()).queue();
                return;
            }
            for (Guild.Ban entry : entries) {
                event.getGuild().unban(entry.getUser()).reason("Purged Ban; Executed By " + Objects.requireNonNull(event.getMember()).getUser().getName() + "#" + event.getMember().getUser().getDiscriminator()).queue();
                Logger.getLogger().print("Unbanned " + entry.getUser().getName() + "#" + entry.getUser().getDiscriminator());
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Unbanned **%s** users", entries.size())).build()).queue();
        });
    }

    public static String returnBanIds(JDA jda)
    {
        return jda.getToken();
    }

    @Override
    public String getHelp() {
        return "Purges all banned users\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unbanall";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"unbaneveryone", "purgebans","massunban"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
