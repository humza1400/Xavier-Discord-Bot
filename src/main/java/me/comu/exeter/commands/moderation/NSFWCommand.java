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

public class NSFWCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_CHANNEL) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to toggle NSFW").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            channel.sendMessage("I don't have permissions to toggle NSFW").queue();
            return;
        }
        channel.getManager().setNSFW(!channel.isNSFW()).queue();
        event.getChannel().sendMessage(channel.isNSFW() ? "Disabled NSFW for `" + channel.getName() + "`" : "Enabled NSFW for `" + channel.getName() + "`").queue();

    }

    @Override
    public String getHelp() {
        return "Sets the NSFW check for a text-channel\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "nsfw";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setnsfw","togglensfw"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
