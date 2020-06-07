package me.comu.exeter.objects;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;

public class RestorableChannel {

    private String name;
    private String id;
    private int position;
    private ChannelType channelType;

    public RestorableChannel(GuildChannel channel) {
        this.name = channel.getName();
        this.id = channel.getId();
        this.position = channel.getPosition();
        this.channelType = channel.getType();

    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public ChannelType getChannelType()
    {
        return channelType;
    }


}

