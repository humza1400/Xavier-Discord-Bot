package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class AutoPurgeImagesCommand implements ICommand {

    public static final List<String> aipChannels = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to set this channel as AIP").queue();
            return;
        }


        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("I don't have permissions to manage messages").queue();
            return;
        }
        if (args.isEmpty()) {
            if (aipChannels.contains(event.getChannel().getId())) {
                event.getChannel().sendMessage("Removed `" + event.getChannel().getName() + "` from the AIP hash").queue();
                aipChannels.remove(event.getChannel().getId());
                return;
            }
            event.getChannel().sendMessage("Added `" + event.getChannel().getName() + "` to the AIP hash").queue();
            aipChannels.add(event.getChannel().getId());
        } else if (args.get(0).equalsIgnoreCase("list")) {
            event.getChannel().sendMessage("**Channels in the AIP Hash**:\n" + aipChannels.toString()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Makes the channel purges all images sent after 5 minutes\n" + "`" + Core.PREFIX + getInvoke() + " [list]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "autoimagepurge";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"aip"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
