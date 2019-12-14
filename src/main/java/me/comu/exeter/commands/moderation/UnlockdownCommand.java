package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class UnlockdownCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MANAGE_CHANNEL) && (!member.hasPermission(Permission.MANAGE_CHANNEL))) {
            channel.sendMessage("You don't have permission to unlockdown the channel").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            channel.sendMessage("I don't have permissions to unlockdown the channel").queue();
            return;
        }
        if (!LockdownCommand.inLockdown)
        {
            event.getChannel().sendMessage(String.format("`%s` is not currently in lockdown.", channel.getName())).queue();
            return;
        }
        try {
            //      PermissionOverride permissionOverride = event.getChannel().getPermissionOverride(everyoneRole);
            //    PermissionOverrideAction manager = permissionOverride.getManager();
            if (!SetLockdownRoleCommand.isIsLockdownRoleSet()) {
                event.getGuild().getPublicRole().getManager().givePermissions(Permission.MESSAGE_WRITE).queue();
            } else
            {
                SetLockdownRoleCommand.getLockdownRole().getManager().givePermissions(Permission.MESSAGE_WRITE).queue();
            }
            LockdownCommand.inLockdown = false;
            event.getChannel().sendMessage(":unlock: Channel has been unlocked!").queue();
        } catch (NullPointerException npe) {
            event.getChannel().sendMessage(npe.getMessage()).queue();
            event.getChannel().sendMessage("Caught an error while trying to lock-down the channel").queue();
        }
    }


    @Override
    public String getHelp() {
        return "Unlocks the current text channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unlock";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"unlockdown","releaselock","uld","openchannel"};
    }
}
