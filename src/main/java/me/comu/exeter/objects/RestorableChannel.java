package me.comu.exeter.objects;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;

public class RestorableChannel {

    private String name;
    private String id;
    private String guildId;
    private int position;
    private ChannelType channelType;

    public RestorableChannel(GuildChannel channel) {
        this.name = channel.getName();
        this.id = channel.getId();
        this.position = channel.getPosition();
        this.channelType = channel.getType();
        this.guildId = channel.getGuild().getId();

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGuildId(){return guildId;}

    public int getPosition() {
        return position;
    }

    public ChannelType getChannelType()
    {
        return channelType;
    }


}

