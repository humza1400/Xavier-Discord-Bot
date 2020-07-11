package me.comu.exeter.events;

import me.comu.exeter.commands.bot.AutoResponseCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageListenerResponderEvent extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getMessage().getAuthor().isBot()) {
            if (!event.getMessage().getMentionedMembers().isEmpty() && event.getMessage().getMentionedMembers().get(0).getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
                MessageEmbed embed = new EmbedBuilder().addField("Current prefix", Core.PREFIX, false).addField("More Information", Core.PREFIX + "help", false).setColor(Utility.getAmbientColor()).build();
                event.getChannel().sendMessage(embed).queue();
            }
            if (AutoResponseCommand.responses.containsKey(event.getMessage().getContentRaw()))
                event.getChannel().sendMessage(AutoResponseCommand.responses.get(event.getMessage().getContentRaw())).queue();

        }

    }
}
