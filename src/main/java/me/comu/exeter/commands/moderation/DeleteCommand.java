package me.comu.exeter.commands.moderation;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DeleteCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_CHANNEL) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to delete channels.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to delete channels.").build()).queue();
            return;
        }
        event.getChannel().sendMessageEmbeds(Utility.embed("Channel will be deleted in a few seconds...").build()).queue(success -> {
            event.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
        });

    }

    @Override
    public String getHelp() {
        return "Deletes the channel\n `" + Core.PREFIX + getInvoke() + "`\nAliases:`" + Arrays.deepToString(getAlias()) +"`";
    }

    @Override
    public String getInvoke() {
        return "delete";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"delchannel","deletechannel"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
