package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class LockdownCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_CHANNEL) && (!member.hasPermission(Permission.MANAGE_CHANNEL)) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to lockdown the channel").build()).queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to lockdown the channel").build()).queue();
            return;
        }
        if (event.getChannel().getPermissionOverride(event.getGuild().getPublicRole()) != null) {
            EnumSet<Permission> deniedPermissions = Objects.requireNonNull(event.getChannel().getPermissionOverride(event.getGuild().getPublicRole())).getManager().getDeniedPermissions();
            if (deniedPermissions.contains(Permission.MESSAGE_WRITE)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Channel is already **locked**!").build()).queue();
            }
        } else {
            event.getChannel().upsertPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed(":lock: Channel has been put on **lockdown**!").build()).queue();
        }

    }


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
        return new String[]{"ld", "lock", "lockchannel"};
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
