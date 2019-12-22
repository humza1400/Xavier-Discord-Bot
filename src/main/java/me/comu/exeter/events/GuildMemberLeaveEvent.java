package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.SetLeaveChannelCommand;
import me.comu.exeter.commands.moderation.ToggleLeaveChannelCommand;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberLeaveEvent extends ListenerAdapter {

        private static final String leave = "[member] left and literally no1 cares";

        public void onGuildMemberLeave(net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent event) {
            Core.jda.getPresence().setActivity(Activity.watching(String.format("over %s users", event.getJDA().getGuildById("645841446817103912").getMembers().size())));
            if (ToggleLeaveChannelCommand.isActive()) {
                EmbedBuilder leaveEmbed = new EmbedBuilder();
                leaveEmbed.setColor(0xd60d28);
                leaveEmbed.setDescription(leave.replace("[member]", event.getMember().getAsMention()));
                if (!SetLeaveChannelCommand.bound)
                event.getGuild().getDefaultChannel().sendMessage(leaveEmbed.build()).queue();
                else
                    event.getGuild().getTextChannelById(SetLeaveChannelCommand.logChannelID).sendMessage(leaveEmbed.build()).queue();
            }
        }


}
