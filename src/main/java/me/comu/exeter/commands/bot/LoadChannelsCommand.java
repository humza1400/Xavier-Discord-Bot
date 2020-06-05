package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class LoadChannelsCommand implements ICommand {
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
