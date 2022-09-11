package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ToggleLeaveChannelCommand implements ICommand {

    private static boolean active = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member memberPerms = event.getMember();
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(getHelp()).build()).queue();
            return;
        }
        if (!Objects.requireNonNull(memberPerms).hasPermission(Permission.MANAGE_SERVER) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle farewell messages").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                event.getChannel().sendMessageEmbeds(Utility.embed("Leave messages will now be sent when a user leaves").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Farewell messages are already enabled").build()).queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessageEmbeds(Utility.embed("Leave messages will no longer be sent").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Farewell messages are already disabled").build()).queue();
        }
    }


    public static boolean isActive() {
        return active;
    }

    @Override
    public String getHelp() {
        return "Sends a message when a user leaves the server\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "leavemessages";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"toggleleave", "leavemessage", "farewellmessage", "farewellmessages", "leavemsgs", "farewellmsgs"};
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
