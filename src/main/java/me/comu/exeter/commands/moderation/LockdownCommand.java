package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LockdownCommand implements ICommand {

    public static boolean inLockdown = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_CHANNEL) && (!member.hasPermission(Permission.MANAGE_CHANNEL)) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to lockdown the channel").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            channel.sendMessage("I don't have permissions to lockdown the channel").queue();
            return;
        }
        try {
         //      PermissionOverride permissionOverride = event.getChannel().getPermissionOverride(everyoneRole);
        //    PermissionOverrideAction manager = permissionOverride.getManager();
        if (!SetLockdownRoleCommand.isIsLockdownRoleSet()) {
            event.getGuild().getPublicRole().getManager().revokePermissions(Permission.MESSAGE_WRITE).queue();
        } else
        {
           SetLockdownRoleCommand.getLockdownRole().getManager().revokePermissions(Permission.MESSAGE_WRITE).queue();
        }
            inLockdown = true;
            event.getChannel().sendMessage(":lock: Channel has been put on lockdown!").queue();
        } catch (NullPointerException npe) {
            event.getChannel().sendMessage(npe.getMessage()).queue();
            event.getChannel().sendMessage("Caught an error while trying to lock-down the channel").queue();
        }
    }

    public static boolean isIsLockdown() {return inLockdown;}

    @Override
    public String getHelp() {
        return "Locks the current text channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lockdown";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"ld"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
