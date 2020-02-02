package me.comu.exeter.commands.misc;

import me.comu.exeter.commands.economy.EconomyManager;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.util.VCTrackingManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class VCStatsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Map<String, Long> minuteMap = new HashMap<>();
        Map<String, Long> hourMap = new HashMap<>();
        for (String x : VCTrackingManager.getJoinedVCUsers().keySet())
        {
            if (VCTrackingManager.getLeaveVCUsers().containsKey(x))
            {
                Instant instantJoined = Instant.ofEpochSecond(VCTrackingManager.getJoinedVCUsers().get(x));
                Instant instantLeave = Instant.ofEpochSecond(VCTrackingManager.getLeaveVCUsers().get(x));
                Duration duration = Duration.between(instantJoined, instantLeave);
                minuteMap.put(x, duration.toMinutes());
                hourMap.put(x, duration.toHours());
            }
        }
        StringBuilder stringBuffer = new StringBuilder();
        LinkedHashMap<String, Long> collectMinutes = minuteMap.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        LinkedHashMap<String, Long> collectHours = hourMap.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        int counter2 = 1;
        for (String x : collectMinutes.keySet()) {
            User user = event.getJDA().getUserById(x);
            if (counter2 != 11) {
                try {
                    String name = user.getName() + "#" + user.getDiscriminator();
                    if (!(minuteMap.get(user.getId()) > 100000) &&  !(minuteMap.get(user.getId()) < 0) && !(hourMap.get(user.getId()) > 100000) &&  !(hourMap.get(user.getId()) < 0))
                    {
                        stringBuffer.append("**" + counter2 + "**. " + name + " : " + hourMap.get(user.getId()) + " hours " + minuteMap.get(user.getId()) + " minutes\n");
                        counter2++;
                    }
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessage("The hash set contains an invalid user, please resolve this issue. (" + x + ")").queue();
                }

            }

        }
        event.getChannel().sendMessage(EmbedUtils.embedMessage("**Most Active VC Leaderbaords:**\n" + stringBuffer.toString()).build()).queue();

    }

    @Override
    public String getHelp() {
        return "Returns the voice-channel leaderboards of the guild\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "vcstats";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"vcstat", "vcleaderboards", "vclb"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
