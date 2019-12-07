package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ServerName implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Member selfMember = event.getGuild().getSelfMember();
        if (args.get(0) == "")
        {
            event.getChannel().sendMessage("Please specify a server name").queue();
            return;
        }
//        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
//            channel.sendMessage("You don't have permission to change the server name").queue();
//            return;
//        }
//
//
//        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) ) {
//            channel.sendMessage("I don't have permissions to change the server name").queue();
//            return;
//        }

            event.getGuild().getManager().setName(msg.replace(";servername", "")).complete();
    }

    @Override
    public String getHelp() {
        return "Sets the server's name\n`" + Core.PREFIX + getInvoke() + " [name]`\nAliases: `" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "servername";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"serservername","nameserver"};
    }
}
