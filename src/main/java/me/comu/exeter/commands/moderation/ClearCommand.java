package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
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
        if (args.get(0).equals("bot") || args.get(0).equals("bots"))
        {
            List<Message> botMessages = new ArrayList<>();
            List<String> botNames = new ArrayList<>();
            for (Message message : event.getChannel().getHistory().retrievePast(100).complete()) {
                if (message.getAuthor().isBot()) {
                    botMessages.add(message);
                    botNames.add(message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator());
                }
            }
            try {
                event.getChannel().deleteMessages(botMessages).complete();
                StringBuffer buffer = new StringBuffer();
                for (String s : botNames)
                    buffer.append(s + ", ");
                removeDuplicates(botNames);
//            int index = botNames.get(botNames.size()).charAt(botNames.get(botNames.size()).length()-1);
//            botNames.get(botNames.size()).replace('')
                event.getChannel().sendMessage(String.format("Deleted `%s` messages by `%s`", botMessages.size(), buffer.toString())).queue();
                List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
                event.getChannel().deleteMessages(messages2).queueAfter(3, TimeUnit.SECONDS);
                return;
            } catch (IllegalArgumentException ex)
            {
                event.getChannel().sendMessage("No bot messages found to purge.").queue();
            }
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

    private List<String> removeDuplicates(List<String> list)
    {
        for (String s : list)
        {
            if (!list.contains(s))
            {
                list.add(s);
            }
        }
        return list;
    }
}
