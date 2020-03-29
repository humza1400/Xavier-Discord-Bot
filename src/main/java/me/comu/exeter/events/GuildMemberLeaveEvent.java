package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.SetLeaveChannelCommand;
import me.comu.exeter.commands.moderation.ToggleLeaveChannelCommand;
import me.comu.exeter.core.Core;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GuildMemberLeaveEvent extends ListenerAdapter {

        private static final String leave = "[member] left and literally no1 cares";

        public void onGuildMemberRemove(net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent event) {
                int amount = 0;
                for (Guild g : event.getJDA().getGuilds())
                {
                    amount += g.getMembers().size();
                }
                Core.jda.getPresence().setActivity(Activity.watching(String.format("over %s users", amount)));

            if (ToggleLeaveChannelCommand.isActive()) {
                EmbedBuilder leaveEmbed = new EmbedBuilder();
                leaveEmbed.setColor(0xd60d28);
                leaveEmbed.setDescription(leave.replace("[member]", Objects.requireNonNull(event.getMember()).getAsMention()));
                if (!SetLeaveChannelCommand.bound)
                Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(leaveEmbed.build()).queue();
                else
                    Objects.requireNonNull(event.getGuild().getTextChannelById(SetLeaveChannelCommand.logChannelID)).sendMessage(leaveEmbed.build()).queue();
            }
        }


}
