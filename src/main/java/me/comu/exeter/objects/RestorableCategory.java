package me.comu.exeter.objects;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;

public class RestorableCategory {

    private int position;
    private String id;
    private String name;
    private HashMap<Integer, String> voiceChannels = new HashMap<>();
    private HashMap<Integer, String> textChannels = new HashMap<>();

    public RestorableCategory(Category category) {
        this.position = category.getPosition();
        this.id = category.getId();
        this.name = category.getName();
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

    public HashMap<Integer, String> getChildTextChannels() {
        return textChannels;
    }

    public HashMap<Integer, String> getChildVoiceChannels() {
        return voiceChannels;
    }

}
