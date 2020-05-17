package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.BindLogChannelCommand;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LogMessageReceivedEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && BindLogChannelCommand.bound && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            DateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
            Date dateobj = new Date();
            String guildName = event.getGuild().getName();
            String channelName = event.getChannel().getName();
            User author = event.getAuthor();
            Message message = event.getMessage();
            try {
                Objects.requireNonNull(event.getJDA().getTextChannelById(BindLogChannelCommand.logChannelID)).sendMessage((String.format("`%s (%s)[%s]<%#s>:` %s", df.format(dateobj.getTime()),guildName, channelName, author, message).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere"))).queue();
            } catch (NullPointerException ex) {
                event.getChannel().sendMessage("SOMETHING WENT WRONG").queue();
            }
        }
    }
}
