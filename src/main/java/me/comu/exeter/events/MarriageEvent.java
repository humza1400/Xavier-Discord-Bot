package me.comu.exeter.events;

import me.comu.exeter.commands.marriage.MarryCommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class MarriageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && MarryCommand.pending) {
            TextChannel textChannel = event.getGuild().getTextChannelById(MarryCommand.marriageChannelID);
            if (event.getChannel().getIdLong() == textChannel.getIdLong()) {
                User author = event.getAuthor();
                if (author.getIdLong() == MarryCommand.getProposedID && event.getMessage().getContentRaw().equalsIgnoreCase("Yes"))
                {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " has accepted " + event.getJDA().getUserById(MarryCommand.getProposerID).getAsMention() + "'s marriage proposal. **Congratulations**!").queue();
                    Wrapper.marriedUsers.put(Long.toString(MarryCommand.getProposerID), Long.toString(MarryCommand.getProposedID));
                    MarryCommand.pending = false;
                } else if (author.getIdLong() == MarryCommand.getProposedID && event.getMessage().getContentRaw().equalsIgnoreCase("No"))
                {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " just rejected " + event.getJDA().getUserById(MarryCommand.getProposerID).getAsMention() + "'s marriage proposal. Maybe next time bro.").queue();
                    MarryCommand.pending = false;
                }
            }
        }
    }
}