package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiRaidChannelSafetyCommand implements ICommand {

    private static boolean active = false;
    public static Map<String, String> channels;
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("You don't have permission to toggle ARCS").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                channels = new HashMap<>();
                for (TextChannel channel : event.getJDA().getTextChannels())
                {
                    channels.put(channel.getId(), Integer.toString(channel.getPositionRaw()));
                }
                for (VoiceChannel channel : event.getJDA().getVoiceChannels())
                {
                    channels.put(channel.getId(), Integer.toString(channel.getPositionRaw()));
                }
                for (net.dv8tion.jda.api.entities.Category channel : event.getJDA().getCategories())
                {
                    channels.put(channel.getId(), Integer.toString(channel.getPositionRaw()));
                }
                event.getChannel().sendMessage("ARCS is now active").queue();
            } else
                event.getChannel().sendMessage("ARCS is already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessage("ARCS is no longer active").queue();
            } else
                event.getChannel().sendMessage("ARCS is already disabled").queue();
        }

    }


    public static boolean isActive()
    {
        return active;
    }

    @Override
    public String getHelp() {
        return "When enabled, all channels deleted get instantly remade (Only works if Anti-Raid is on)\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "antiraidchannelsafety";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"arcs"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }


}
