package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.Arrays;
import java.util.List;

public class ServerNameCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        String preServerName = event.getGuild().getName();
        if (!member.hasPermission(Permission.MANAGE_SERVER) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to change the server name").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("I don't have permissions to change the server name").queue();
            return;
        }
        String msg = event.getMessage().getContentRaw();
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a server name").queue();
            return;
        }
        msg = msg.replace(Core.PREFIX + "servername", "").replace(Core.PREFIX + "nameserver", "").replace("_", " ");
        if (msg.length() != 2) {
            event.getGuild().getManager().setName(msg).queue();
            event.getChannel().sendMessage("Successfully changed server name from `" + preServerName + "` to `" + msg.substring(1) + "`.").queue();
        } else
            event.getChannel().sendMessage("Server names must be at least two characters long.").queue();
    }

    @Override
    public String getHelp() {
        return "Sets the server name\n`" + Core.PREFIX + getInvoke() + " [name]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "servername";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"nameserver"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
