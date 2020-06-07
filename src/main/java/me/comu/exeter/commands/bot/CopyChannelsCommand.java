package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.RestorableCategory;
import me.comu.exeter.objects.RestorableChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class CopyChannelsCommand implements ICommand {

    static List<RestorableCategory> restorableCategories = new ArrayList<>();
    static List<RestorableChannel> restorableChannels = new ArrayList<>();
    static boolean copied = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to copy the channels").queue();
            return;
        }
        clearCopiedChannels();
        event.getGuild().getCategories().forEach(category -> restorableCategories.add(new RestorableCategory(category)));
        event.getGuild().getChannels().stream().filter((guildChannel -> guildChannel.getParent() == null)).forEach(guildChannel -> restorableChannels.add(new RestorableChannel(guildChannel)));
        copied = true;
        event.getChannel().sendMessage("Successfully copied `" + event.getGuild().getTextChannels().size() + "` text channels, `" + event.getGuild().getVoiceChannels().size() + "` voice channels, and `" + event.getGuild().getCategories().size() + "` categories.").queue();


    }

    static void clearCopiedChannels()
    {
        restorableCategories.clear();
        restorableChannels.clear();
    }

    @Override
    public String getHelp() {
        return "Copies the current guild's channels\n `" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "copychannels";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"channelcopy", "channelscopy", "copychannel", "savechannels"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
