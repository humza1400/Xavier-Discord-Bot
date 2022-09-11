package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetLeaveChannelCommand implements ICommand {

    public static boolean bound = false;
    public static long logChannelID;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_CHANNEL) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set the farewell channel").build()).queue();
            return;
        }
        if (bound) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Farewell channel already bound. Nullifying...").build()).queue();
        }
        TextChannel channel = event.getChannel();
        logChannelID = channel.getIdLong();
        event.getChannel().sendMessageEmbeds(Utility.embed("Farewell channel bound to `#" + event.getChannel().getName() + "`").build()).queue();
        bound = true;
    }

    @Override
    public String getHelp() {
        return "Binds the current text-channel to send farewell messages\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setfarewell";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setleave","setfarewellchannel","setfarewellmessages","setleavemessages"};
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
