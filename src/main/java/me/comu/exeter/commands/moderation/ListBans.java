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

public class ListBans implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You don't have permission to unban users").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have permissions to unban users").queue();
            return;
        }

        List<Guild.Ban> banList = event.getGuild().retrieveBanList().complete();
        if (banList.isEmpty()) {
            channel.sendMessage("There are no users currently banned!").queue();
            return;
        }
        StringBuffer buffer = new StringBuffer(event.getGuild().getName() + " Banlist (" +banList.size() + ")\n");
        for (int i = 0; i < banList.size(); i++) {
            buffer.append(" + " + banList.get(i).getUser().getName() + "#" +  banList.get(i).getUser().getDiscriminator() + " | " + banList.get(i).getReason() + "\n");
        }
        event.getChannel().sendMessage(buffer.toString()).queue();
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
}
