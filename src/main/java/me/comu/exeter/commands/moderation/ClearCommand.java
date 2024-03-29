package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClearCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MESSAGE_MANAGE) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to purge messages").build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to purge messages").build()).queue();
            return;
        }


        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Insert an amount of messages to purge").build()).queue();
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


                    event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Deleted `%s` messages by `%s`", botMessages.size(), buffer)).build()).queue((message -> {
                        event.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    }));
                } catch (IllegalArgumentException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("No bot messages found to purge.").build()).queue();
                }
            }));
            return;
        } else if (args.get(0).equalsIgnoreCase("image") || args.get(0).equalsIgnoreCase("images")) {
            event.getChannel().getHistory().retrievePast(100).queue((messages -> {
                List<Message> messageStream = messages.stream().filter(message -> message.getAttachments().size() > 0).collect(Collectors.toList());
                int size = messageStream.size();
                messageStream.forEach(message -> message.delete().queue());
                event.getChannel().sendMessageEmbeds(Utility.embed("Deleted `" + size + "` messages that contained images").build()).queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));

            }));
            return;
        } else if (args.get(0).equalsIgnoreCase("embed") || args.get(0).equalsIgnoreCase("embeds")) {
            event.getChannel().getHistory().retrievePast(100).queue((messages -> {
                List<Message> messageStream = messages.stream().filter(message -> message.getEmbeds().size() > 0).collect(Collectors.toList());
                int size = messageStream.size();
                messageStream.forEach(message -> message.delete().queue());
                event.getChannel().sendMessageEmbeds(Utility.embed("Deleted `" + size + "` messages that contained embeds").build()).queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));

            }));
            return;
        }


        try {
            if (Integer.parseInt(args.get(0)) >= 100) {
                event.getMessage().delete().queue(onDelete -> {
                    event.getChannel().getHistory().retrievePast(100).queue(messages -> event.getChannel().purgeMessages(messages));
                    event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Cleared %s messages :champagne_glass:", args.get(0))).build()).queue(tempMessage -> tempMessage.delete().queueAfter(2, TimeUnit.SECONDS));
                });
            } else {
                event.getMessage().delete().queue(onDelete -> {
                    event.getChannel().getHistory().retrievePast(Integer.parseInt(args.get(0))).queue((tempMessages) -> event.getChannel().purgeMessages(tempMessages));
                    event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Cleared %s messages :champagne_glass:", args.get(0))).build()).queue(tempMessage -> tempMessage.delete().queueAfter(2, TimeUnit.SECONDS));
                });
            }
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid number of messages to purge or purge the bot/images messages.").build()).queue();
        }
    }

//    public void purgePaginatedMessages(MessageChannel channel, int amount, Consumer<List<Message>> callback) {
//        List<Message> messages = new ArrayList<>(amount);
//        channel.getIterableHistory().cache(false).forEachAsync((message) ->
//        {
//            messages.add(message);
//            return messages.size() < amount;
//        }).thenRun(() -> callback.accept(messages));
//    }


    @Override
    public String getHelp() {
        return "Purges the specified amount of messages\n `" + Core.PREFIX + getInvoke() + "` [amount]/[bots]/[images]\nAliases:`" + Arrays.deepToString(getAlias()) + "`";
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

    @Override
    public boolean isPremium() {
        return false;
    }

}
