package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class DisconnectUserCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!event.getMember().hasPermission(Permission.VOICE_DEAF_OTHERS)) {
            event.getChannel().sendMessage("You don't have permission to disconnect a user from VC").queue();
            return;
        }

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("Please specify a user to disconnect from VC").queue();
            return;
        }
        Member member = mentionedMembers.get(0);
        if (member.getVoiceState().inVoiceChannel())
        {
         member.deafen(true);

        }

    }

    @Override
    public String getHelp() {
        return "Deafens a user from voice channel\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "deafen";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"vcmute","vcmute"};
    }
}
