package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SlowmodeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        int slowtime = 0;

        if (!member.hasPermission(Permission.MANAGE_CHANNEL) && (!member.hasPermission(Permission.MANAGE_CHANNEL)) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to unlockdown the channel").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            channel.sendMessage("I don't have permissions to unlockdown the channel").queue();
            return;
        }
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert an amount to set the slowmode to!").queue();
            return;
        }
        try {
                slowtime = Integer.parseInt(args.get(0));
        } catch (Exception ex) {
            channel.sendMessage("That number is either invalid or too large to change the volume to or is a floating point number (integers only)").queue();
            return;
        }
        channel.getManager().setSlowmode(slowtime).queue();
        channel.sendMessage(String.format("`%s` has been put on slowmode for `%s` seconds!", channel.getName(), slowtime)).queue();
    }

    @Override
    public String getHelp() {
        return "Sets the slowmode time of a text-channel\n`" + Core.PREFIX + getInvoke() + " [amount]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "slowmode";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"slow","setslow"};
    }
}
