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

public class UnlockdownCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_CHANNEL) && (!member.hasPermission(Permission.MANAGE_CHANNEL)) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to unlockdown the channel").build()).queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to unlockdown the channel").build()).queue();
            return;
        }
        if (event.getChannel().getPermissionOverride(event.getGuild().getPublicRole()) != null) {
            EnumSet<Permission> deniedPermissions = Objects.requireNonNull(event.getChannel().getPermissionOverride(event.getGuild().getPublicRole())).getManager().getDeniedPermissions();
            if (!deniedPermissions.contains(Permission.MESSAGE_WRITE)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Channel is already **unlocked**!").build()).queue();
            } else {
                event.getChannel().upsertPermissionOverride(event.getGuild().getPublicRole()).clear(Permission.MESSAGE_WRITE).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(":unlock: Channel has been **unlocked**!").build()).queue();
            }
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " Channel is already **unlocked**!").build()).queue();
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
        return new String[]{"unlockdown", "releaselock", "unlockdownchannel", "unlockchannel", "uld", "openchannel"};
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
