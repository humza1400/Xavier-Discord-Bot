package me.comu.exeter.objects;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;

public class RestorableCategory {

    private final int position;
    private final String id;
    private final String guildId;
    private final String name;
    private final HashMap<Integer, String> voiceChannels = new HashMap<>();
    private final HashMap<Integer, String> textChannels = new HashMap<>();

    public RestorableCategory(Category category) {
        this.position = category.getPosition();
        this.id = category.getId();
        this.name = category.getName();
        this.guildId = category.getGuild().getId();
        category.getChannels().stream().filter((guildChannel -> (guildChannel instanceof TextChannel))).forEach((guildChannel -> this.textChannels.put(guildChannel.getPosition(), guildChannel.getName())));
        category.getChannels().stream().filter((guildChannel -> (guildChannel instanceof VoiceChannel))).forEach((guildChannel -> this.voiceChannels.put(guildChannel.getPosition(), guildChannel.getName())));
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public String getGuildId() {
        return guildId;
    }

    public HashMap<Integer, String> getChildTextChannels() {
        return textChannels;
    }

    public HashMap<Integer, String> getChildVoiceChannels() {
        return voiceChannels;
    }

}
