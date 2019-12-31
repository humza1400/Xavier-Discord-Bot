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

public class ListBansCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.BAN_MEMBERS) && event.getMember().getIdLong() != Core.OWNERID) {
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
            StringBuffer buffer = new StringBuffer(event.getGuild().getName() + " Banlist (" +entries.size() + ")\n");
            for (int i = 0; i < entries.size(); i++) {
                buffer.append(" + " + entries.get(i).getUser().getName() + "#" +  entries.get(i).getUser().getDiscriminator() + " | " + entries.get(i).getReason() + "\n");
            }
            if (buffer.toString().length() < 2000)
            event.getChannel().sendMessage(buffer.toString()).queue();
            else
                event.getChannel().sendMessage("Too many bans to display.").queue();
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
