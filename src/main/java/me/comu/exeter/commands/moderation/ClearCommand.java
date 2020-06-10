package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ClearCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MESSAGE_MANAGE) && member.getIdLong() != Core.OWNERID) {
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
        if (args.get(0).equals("bot") || args.get(0).equals("bots")) {
            List<Message> botMessages = new ArrayList<>();
            List<String> botNames = new ArrayList<>();
            event.getChannel().getHistory().retrievePast(100).queue((botmessagesforbuffer -> {
                for (Message message : botmessagesforbuffer) {
                    if (message.getAuthor().isBot()) {
                        botMessages.add(message);
                        botNames.add(message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator());
                    }
                }
                try {
                    event.getChannel().deleteMessages(botMessages).queue();
                    StringBuilder buffer = new StringBuilder();
                    Set<String> set = new HashSet<>(botNames);
                    botNames.clear();
                    botNames.addAll(set);
                    for (String s : botNames) {
                        buffer.append(s).append(", ");
                    }
                    buffer.setCharAt(buffer.length() - 2, '.');
                    event.getChannel().sendMessage(String.format("Deleted `%s` messages by `%s`", botMessages.size(), buffer.toString())).queue((message -> {
                        event.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    }));
                } catch (IllegalArgumentException ex) {
                    event.getChannel().sendMessage("No bot messages found to purge.").queue();
                }
            }));
            return;
        }
        try {
            if (Integer.parseInt(args.get(0)) > 100) {
                event.getMessage().delete().queue(onDelete -> {
                    purgePaginatedMessages(channel, Integer.parseInt(args.get(0)), messages -> channel.purgeMessages());
                    event.getChannel().sendMessage(String.format("Cleared %s messages :champagne_glass:", args.get(0))).queue(tempMessage -> tempMessage.delete().queueAfter(2, TimeUnit.SECONDS));
                });
            } else {
                event.getMessage().delete().queue(onDelete -> {
                    event.getChannel().getHistory().retrievePast(Integer.parseInt(args.get(0))).queue((tempMessages) -> event.getChannel().purgeMessages(tempMessages));
                    event.getChannel().sendMessage(String.format("Cleared %s messages :champagne_glass:", args.get(0))).queue(tempMessage -> tempMessage.delete().queueAfter(2, TimeUnit.SECONDS));
                });
            }
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("Please insert a valid number of messages to purge or purge the bot messages.").queue();
        }
    }

    public void purgePaginatedMessages(MessageChannel channel, int amount, Consumer<List<Message>> callback) {
        List<Message> messages = new ArrayList<>(amount);
        channel.getIterableHistory().cache(false).forEachAsync((message) ->
        {
            messages.add(message);
            return messages.size() < amount;
        }).thenRun(() -> callback.accept(messages));
    }


    @Override
    public String getHelp() {
        return "Purges the specified amount of messages\n `" + Core.PREFIX + getInvoke() + "` [amount]\nAliases:`" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"purge"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

}
