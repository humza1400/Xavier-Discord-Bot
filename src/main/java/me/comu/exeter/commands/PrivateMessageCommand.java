package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrivateMessageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
            String message = event.getMessage().getContentRaw();
            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.getChannel().sendMessage("You don't have permission to private message someone").queue();
                return;
            }

            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessage("Please specify a user to private message").queue();
                return;
            }
        int subIndex = Core.PREFIX.length() + getInvoke().length() + mentionedMembers.get(0).getAsMention().length();
        String postMessage = message.substring(subIndex, message.length());
        List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
        event.getChannel().deleteMessages(messages2).queueAfter(50, TimeUnit.MILLISECONDS);
            sendPrivateMessage(mentionedMembers.get(0).getUser(), postMessage);
            event.getChannel().sendMessage(EmbedUtils.embedMessage("Successfully messaged " + mentionedMembers.get(0).getEffectiveName()).build()).queue();
        }

    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

    @Override
    public String getHelp() {
        return "Private messages the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dm";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"pm", "message"};
        }

    }