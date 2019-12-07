package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Message> messages;
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("You don't have permission to purge messages").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("I don't have permissions to purge messages").queue();
            return;
        }


        if (args.isEmpty()) {
                event.getChannel().sendMessage("Insert an amount of messages to purge").queue();
                return;
            }
            if (Integer.parseInt(args.get(0)) > 100)
                messages = event.getChannel().getHistory().retrievePast(100).complete();
            else
                messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args.get(0))).complete();
                event.getChannel().purgeMessages(messages);
            if (Integer.parseInt(args.get(0)) == 1) {
                event.getChannel().sendMessage(String.format("Cleared %s message 🥂", args.get(0))).queue();
            } else {
                event.getChannel().sendMessage(String.format("Cleared %s messages 🥂", args.get(0))).queue();
            }
            List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
            event.getChannel().deleteMessages(messages2).queueAfter(3, TimeUnit.SECONDS);

    }

    @Override
    public String getHelp() {
        return "Purges the specified amount of messages\n `" + Core.PREFIX + getInvoke() + "` [amount]\nAliases:`" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"purge"};
    }
}
