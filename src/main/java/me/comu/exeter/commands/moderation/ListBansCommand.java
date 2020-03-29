package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListBansCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.BAN_MEMBERS) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to unban users").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have permissions to unban users").queue();
            return;
        }

        event.getGuild().retrieveBanList().queue((entries) -> {
            if (entries.isEmpty()) {
                channel.sendMessage("There are no users currently banned!").queue();
                return;
            }
            StringBuilder buffer = new StringBuilder(event.getGuild().getName() + " Banlist (" +entries.size() + ")\n");
            for (Guild.Ban entry : entries) {
                buffer.append(" + ").append(entry.getUser().getName()).append("#").append(entry.getUser().getDiscriminator()).append(" | ").append(entry.getReason()).append("\n");
            }
            if (buffer.toString().length() < 2000)
            event.getChannel().sendMessage(buffer.toString()).queue();
            else
                event.getChannel().sendMessage("Too many bans to display. (" + entries.size() + ")").queue();
        });
    }

    @Override
    public String getHelp() {
        return "Lists all banned users\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "listbans";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"banlist","bans"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
