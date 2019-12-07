package me.comu.exeter.events;

import me.comu.exeter.commands.SetLeaveChannelCommand;
import me.comu.exeter.commands.ToggleLeaveChannelCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberLeaveEvent extends ListenerAdapter {

        private static final String leave = "[member] left and literally no1 cares";

        public void onGuildMemberLeave(net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent event) {
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
