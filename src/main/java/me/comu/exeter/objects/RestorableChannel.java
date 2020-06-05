package me.comu.exeter.objects;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;

public class RestorableChannel {

    private GuildChannel guildChannel;

    public RestorableChannel(GuildChannel channel) {
        this.guildChannel = channel;
    }

    public String getName() {
        return guildChannel.getName();
    }

    public String getID() {
        return guildChannel.getId();
    }

    public int getPosition() {
        return guildChannel.getPosition();
    }

    public ChannelType getChannelType()
    {
        return guildChannel.getType();
    }


}

