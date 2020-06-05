package me.comu.exeter.objects;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;

public class RestorableCategory {

    private Category category;

    public RestorableCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return category.getName();
    }

    public int getPosition() {
        int position = category.getPosition();
        return position;
    }

    public String getId() {
        return category.getId();
    }

    public HashMap<Integer, String> getChildTextChannels() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        if (category.getChannels().isEmpty())
            return hashMap;
        category.getChannels().stream().filter((guildChannel -> (guildChannel instanceof TextChannel))).forEach((guildChannel -> hashMap.put(guildChannel.getPosition(), guildChannel.getName())));
        return hashMap;
    }

    public HashMap<Integer, String> getChildVoiceChannels() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        if (category.getChannels().isEmpty())
            return hashMap;
        category.getChannels().stream().filter((guildChannel -> (guildChannel instanceof VoiceChannel))).forEach((guildChannel -> hashMap.put(guildChannel.getPosition(), guildChannel.getName())));
        return hashMap;
    }

}
