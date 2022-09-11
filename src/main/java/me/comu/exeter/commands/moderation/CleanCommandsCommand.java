package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CleanCommandsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            List<Message> deletedMessages = new ArrayList<>();
            event.getChannel().getHistory().retrievePast(100).queue((messages -> {
                for (Message message : messages) {
                    if (message.getContentRaw().startsWith(Core.PREFIX) || message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                        deletedMessages.add(message);
                    }
                }
                event.getChannel().purgeMessages(deletedMessages);
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleaned up **" + deletedMessages.size() + "** messages.").build()).queue(success -> {
                    if (success != null)
                        success.delete().queueAfter(5, TimeUnit.SECONDS);
                });
                event.getChannel().getHistory().retrievePast(100).queue((cleanMessages) -> {
                    for (Message message : cleanMessages) {
                        if (message.getContentRaw().startsWith(Core.PREFIX) || message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                            deletedMessages.add(message);
                        }
                    }
                    event.getChannel().purgeMessages(deletedMessages);
                });
            }));
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (!memberList.isEmpty() && !args.isEmpty()) {
            List<Message> deletedMessages = new ArrayList<>();
            event.getChannel().getHistory().retrievePast(100).queue((messages -> {
                for (Message message : messages) {
                    if (message.getAuthor().getId().equals(memberList.get(0).getId())) {
                        deletedMessages.add(message);
                    }
                }
                event.getChannel().purgeMessages(deletedMessages);
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleaned up **" + deletedMessages.size() + "** messages from **" + memberList.get(0).getUser().getAsTag() + "**.").build()).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                event.getChannel().getHistory().retrievePast(100).queue((cleanMessages) -> {
                    for (Message message : cleanMessages) {
                        if (message.getContentRaw().startsWith(Core.PREFIX) || message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                            deletedMessages.add(message);
                        }
                    }
                    event.getChannel().purgeMessages(deletedMessages);
                });
            }));
        }
        if (!args.isEmpty() && memberList.isEmpty()) {
            List<Message> deletedMessages = new ArrayList<>();
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            }
            event.getChannel().getHistory().retrievePast(100).queue((messages -> {
                for (Message message : messages) {
                    if (message.getAuthor().getId().equals(targets.get(0).getId())) {
                        deletedMessages.add(message);
                    }
                }
                event.getChannel().purgeMessages(deletedMessages);
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleaned up **" + deletedMessages.size() + "** messages from **" + targets.get(0).getUser().getAsTag() + "**.").build()).queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                event.getChannel().getHistory().retrievePast(100).queue((cleanMessages) -> {
                    for (Message message : cleanMessages) {
                        if (message.getContentRaw().startsWith(Core.PREFIX) || message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                            deletedMessages.add(message);
                        }
                    }
                    event.getChannel().purgeMessages(deletedMessages);
                });
            }));
        }
    }

    @Override
    public String getHelp() {
        return "Cleans ALL bot messages or the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "clean";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"cclean", "cleanmessages", "cleanmsgs"};
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
