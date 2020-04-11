package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
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
            for (String x : VCTrackingManager.getJoinedVCUsers().keySet()) {
                if (VCTrackingManager.getLeaveVCUsers().containsKey(x)) {
                    Instant instantJoined = Instant.ofEpochSecond(VCTrackingManager.getJoinedVCUsers().get(x));
                    Instant instantLeave = Instant.ofEpochSecond(VCTrackingManager.getLeaveVCUsers().get(x));
                    Duration duration = Duration.between(instantJoined, instantLeave);
                    minuteMap.put(x, duration.toMinutes());
                    hourMap.put(x, duration.toHours());
                }
            }
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            StringBuilder stringBuffer = new StringBuilder();
            LinkedHashMap<String, Long> collectMinutes = minuteMap.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            int counter2 = 1;
            for (String x : collectMinutes.keySet()) {
                User user = event.getJDA().getUserById(x);
                if (counter2 != 11) {
                    try {
                        String name = Objects.requireNonNull(user).getName() + "#" + user.getDiscriminator().replaceAll("`","\\`");
                        if (!(minuteMap.get(user.getId()) > 100000) && !(minuteMap.get(user.getId()) < 0) && !(hourMap.get(user.getId()) > 100000) && !(hourMap.get(user.getId()) < 0)) {
                            stringBuffer.append("**").append(counter2).append("**. ").append(name).append(" : ").append(hourMap.get(user.getId())).append(" hours ").append(minuteMap.get(user.getId()) % 60).append(" minutes\n");
                            counter2++;
                        }
                    } catch (NullPointerException ex) {
                        event.getChannel().sendMessage("The hash set contained an invalid user and has been automatically resolved. (" + x + ")").queue();
                        VCTrackingManager.removeJoinedUser(x);
                        VCTrackingManager.removeLeaveUser(x);
                    }

                }

            }
            event.getChannel().sendMessage(EmbedUtils.embedMessage("**Most Active VC Leaderbaords:**\n" + stringBuffer.toString()).build()).queue();
        } else {
            if (Objects.requireNonNull(event.getMessage().getMentionedMembers().get(0).getVoiceState()).inVoiceChannel())
            {
                event.getChannel().sendMessage("You cannot view specific vc-stats while in a vc-channel").queue();
                return;
            }
            try {
                event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " has been in VC for **" + hourMap.get(event.getMessage().getMentionedMembers().get(0).getId()) + "** hours **" + (minuteMap.get(event.getMessage().getMentionedMembers().get(0).getId()) % 60) + "** minutes.").queue();
            } catch (NullPointerException ex)
            {
                event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " has not joined VC.").queue();
            }
        }
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
        return new String[]{"vcstat", "vcleaderboards", "vclb","vcstats"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
