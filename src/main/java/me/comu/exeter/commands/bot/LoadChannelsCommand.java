package me.comu.exeter.commands.bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LoadChannelsCommand implements ICommand {

    private EventWaiter eventWaiter;

    public LoadChannelsCommand(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to load channels").queue();
            return;
        }
        if (!Objects.requireNonNull(event.getGuild().getSelfMember()).hasPermission(Permission.MANAGE_CHANNEL)) {
            event.getChannel().sendMessage("I don't have permission to load channels").queue();
            return;
        }
        if (!CopyChannelsCommand.copied) {
            event.getChannel().sendMessage("No channels are copied").queue();
            return;
        }

//        event.getChannel().sendMessage(new EmbedBuilder().setTitle("\u26A0 Warning").setDescription("Are you sure you want to load this backup? **All channels will get deleted and reconstructed from the backup!**").setColor(Color.YELLOW).build()).queue((message -> message.addReaction("\u2705").queue((success -> initWaiter(event.getAuthor().getId(), message.getId(), event.getChannel().getId(), event.getJDA(), event)))));
        loadChannels(event);
    }

    private void loadChannels(GuildMessageReceivedEvent event) {

        CopyChannelsCommand.copied = false;
        if (!event.getGuild().getChannels().isEmpty())
            event.getGuild().getChannels().forEach(guildChannel -> guildChannel.delete().queue());
        CopyChannelsCommand.restorableChannels.forEach(restorableChannel -> {
            if (restorableChannel.getChannelType().equals(ChannelType.TEXT)) {
                event.getGuild().createTextChannel(restorableChannel.getName()).setPosition(restorableChannel.getPosition()).queue();
            } else if (restorableChannel.getChannelType().equals(ChannelType.VOICE)) {
                event.getGuild().createVoiceChannel(restorableChannel.getName()).setPosition(restorableChannel.getPosition()).queue();
            }
        });
        CopyChannelsCommand.restorableCategories.forEach((restorableCategory -> event.getGuild().createCategory(restorableCategory.getName()).setPosition(restorableCategory.getPosition()).queue((category -> {
            HashMap<Integer, String> hashMap = restorableCategory.getChildTextChannels();
            for (Map.Entry entry : hashMap.entrySet()) {
                event.getGuild().createTextChannel((String) entry.getValue()).setParent(category).setPosition((int) entry.getKey()).queue();
            }
            HashMap<Integer, String> hashMap2 = restorableCategory.getChildVoiceChannels();
            for (Map.Entry entry : hashMap2.entrySet()) {
                event.getGuild().createVoiceChannel((String) entry.getValue()).setParent(category).setPosition((int) entry.getKey()).queue();
            }
        }))));
        CopyChannelsCommand.clearCopiedChannels();

    }

    private void initWaiter(String author, String message, String channel, JDA jda, GuildMessageReceivedEvent msgRecieveEvent) {
        eventWaiter.waitForEvent(GuildMessageReactionAddEvent.class, (event) -> {
            MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
            User user = event.getUser();
            return !user.isBot() && author.equals(user.getId()) && event.getMessageId().equals(message) && reactionEmote.getEmoji().equals("\u2705");
        }, (event) -> {
            TextChannel textChannel = event.getJDA().getTextChannelById(channel);
            try {
                Objects.requireNonNull(textChannel).retrieveMessageById(message).queue(message1 -> message1.editMessage(event.getUser().getAsMention() + " has loaded in a backup!").override(true).queue());
                loadChannels(msgRecieveEvent);
            } catch (NullPointerException ex) {
                Objects.requireNonNull(textChannel).sendMessage("Something went wrong, try again!").queue();
            }
        }, 10, TimeUnit.SECONDS, () -> {
            TextChannel textChannel = jda.getTextChannelById(channel);
            try {
                Objects.requireNonNull(textChannel).retrieveMessageById(message).queue(message1 -> {
                    message1.editMessage("Backup was never confirmed, channel load cancelled.").override(true).queue();
                    message1.removeReaction("\u2705").queue();
                    message1.addReaction("\u274C").queue();
                });

            } catch (NullPointerException ex) {
                Objects.requireNonNull(textChannel).sendMessage("Something went wrong, try again!").queue();
            }
        });
    }

    @Override
    public String getHelp() {
        return "Loads the copied channels into your server\n `" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "loadchannels";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"channelsload", "channelload"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
