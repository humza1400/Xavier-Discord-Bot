package me.comu.exeter.events;

import me.comu.exeter.commands.BindLogChannelCommand;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMessageReceivedEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && BindLogChannelCommand.bound && !event.getMessage().getAuthor().isBot()) {
            DateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
            Date dateobj = new Date();
            TextChannel textChannel = event.getGuild().getTextChannelById(BindLogChannelCommand.logChannelID);
            String guildName = event.getGuild().getName();
            String channelName = event.getChannel().getName();
            User author = event.getAuthor();
            Message message = event.getMessage();
            try {
                textChannel.sendMessage((String.format("`%s (%s)[%s]<%#s>:` %s", df.format(dateobj.getTime()),guildName, channelName, author, message))).queue();
            } catch (NullPointerException ex) {
                event.getChannel().sendMessage((String.format("`%s (%s)[%s]<%#s>:` %s", df.format(dateobj.getTime()),guildName, channelName, author, message))).queue();
            }
        }
    }
}
