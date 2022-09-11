package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WelcomePingCommand implements ICommand {

    public static Map<String, List<String>> welcomePingChannels = new ConcurrentHashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_CHANNEL) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set welcome-ping channels.").build()).queue();
            return;
        }
        if (welcomePingChannels.containsKey(event.getGuild().getId())) {
            List<String> channels = welcomePingChannels.get(event.getGuild().getId());
            if (channels.contains(event.getChannel().getId())) {
                channels.remove(event.getChannel().getId());
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("New members will **no longer** be pinged in this channel.").build()).queue();
            } else {
                channels.add(event.getChannel().getId());
                event.getChannel().sendMessageEmbeds(Utility.embed("New members will **now** be pinged in this channel.").build()).queue();
            }
        } else {
            welcomePingChannels.put(event.getGuild().getId(), new ArrayList<>());
            welcomePingChannels.get(event.getGuild().getId()).add(event.getChannel().getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("New members will **now** be pinged in this channel.").build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Sets the current channel to ping new users on-join\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "welcomeping";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"welcomepingchannel", "setwelcomepingchannel", "joinping"};
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
