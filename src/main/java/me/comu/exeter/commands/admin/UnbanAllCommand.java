package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class UnbanAllCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.ADMINISTRATOR) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to unban users").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("I don't have permissions to unban users").queue();
            return;
        }

        event.getGuild().retrieveBanList().queue((entries) -> {
            if (entries.isEmpty()) {
                channel.sendMessage("There are no users currently banned!").queue();
                return;
            }
            for (int i = 0; i < entries.size(); i++) {
                event.getGuild().unban(entries.get(i).getUser()).queue();
                Logger.getLogger().print("Unbanned " + entries.get(i).getUser().getName() + "#" + entries.get(i).getUser().getDiscriminator());
            }
            event.getChannel().sendMessage(String.format("Unbanned **%s** users", entries.size())).queue();
        });
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
        return new String[]{"unbaneveryone", "purgebans"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
