package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.*;

public class InventoryCommand implements ICommand {

    public static List<String> protection = new ArrayList<>();
    public static List<String> glock = new ArrayList<>();
    public static List<String> draco = new ArrayList<>();
    public static HashMap<String, Integer> ammo = new HashMap<>();
    public static HashMap<String, Integer> shield = new HashMap<>();
    public static HashMap<String, Integer> ecstasy = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(event.getAuthor().getName() + "'s Inventory");
            embedBuilder.setThumbnail(event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setColor(Utility.getAmbientColor());
            embedBuilder.setDescription("**Protection** - " + (protection.contains(Objects.requireNonNull(event.getMember()).getId()) ? "`True`" : "`False`")+"\n**Shield** - " + (shield.containsKey(Objects.requireNonNull(event.getMember()).getId()) ? "`" + shield.get(event.getMember().getId()) + "`" : "`None`") + "\n**Draco** - " + (draco.contains(event.getMember().getId()) ? "`Strapped with it`" : "`Ain't strapped with it`")+"\n**Glock** - " + (glock.contains(event.getMember().getId()) ? "Got it on me" : "`I'mma get caught lackin`") + "\n**Ammo** - " + (ammo.containsKey(event.getMember().getId()) ? "`" + ammo.get(event.getMember().getId()) + "`" : "`None`") + "\n**Ecstasy** - " + (ecstasy.containsKey(event.getMember().getId()) ? "`" + ecstasy.get(event.getMember().getId()) + "`" : "`None`"));
            embedBuilder.setTimestamp(Instant.now());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            event.getChannel().sendMessage("**Protection** - " + (protection.contains(Objects.requireNonNull(event.getMember()).getId()) ? "`True`" : "`False`")+"\n**Shield** - " + (shield.containsKey(Objects.requireNonNull(event.getMember()).getId()) ? "`" + shield.get(event.getMember().getId()) + "`" : "`None`") + "\n**Draco** - " + (draco.contains(event.getMember().getId()) ? "`Strapped with it`" : "`Ain't strapped with it`")+"\n**Glock** - " + (glock.contains(event.getMember().getId()) ? "Got it on me" : "`I'mma get caught lackin`") + "\n**Ammo** - " + (ammo.containsKey(event.getMember().getId()) ? "`" + ammo.get(event.getMember().getId()) + "`" : "`None`") + "\n**Ecstasy** - " + (ecstasy.containsKey(event.getMember().getId()) ? "`" + ecstasy.get(event.getMember().getId()) + "`" : "`None`")).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Shows the items in your inventory\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "backpack";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"inv", "inventory"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
