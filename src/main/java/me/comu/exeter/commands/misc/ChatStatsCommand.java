package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.ChatTrackingManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatStatsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            StringBuilder stringBuffer = new StringBuilder();
            LinkedHashMap<String, Integer> collect = ChatTrackingManager.getChatUsers().entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            int counter2 = 1;
            for (String x : collect.keySet()) {
                User user = event.getJDA().getUserById(x);
                if (counter2 != 11)
                    try {
                        String name = user.getName() + "#" + user.getDiscriminator();
                        stringBuffer.append("**" + counter2 + "**. " + name + " - " + ChatTrackingManager.getChatCredits(user.getId()) + " messages\n");
                        counter2++;
                    } catch (NullPointerException ex) {
                        event.getChannel().sendMessage("The hash set contains an invalid user, please resolve this issue. (" + x + ")").queue();
                    }

            }
            event.getChannel().sendMessage(EmbedUtils.embedMessage("**Most Active Chatters:**\n" + stringBuffer.toString()).build()).queue();
        } else {
            event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " has sent **" + ChatTrackingManager.getChatCredits(event.getMessage().getMentionedMembers().get(0).getId()) + "** messages.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Shows the top 10 people who have sent the most amount of messages\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "chatstats";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"chatleaderboards", "chatleaderboard", "chatlbs", "chatlb"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
