package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DisconnectUserCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.VOICE_MOVE_OTHERS) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to disconnect a user from VC").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.VOICE_MOVE_OTHERS)) {
            event.getChannel().sendMessage("I don't have permissions to disconnect that user").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user to disconnect from VC").queue();
            return;
        }
        if (!args.isEmpty() && mentionedMembers.isEmpty())
        {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            if (Objects.requireNonNull(targets.get(0).getVoiceState()).inVoiceChannel())
            {
                event.getGuild().kickVoiceMember(targets.get(0)).queue();
                event.getChannel().sendMessage("Disconnected " + targets.get(0).getAsMention() + " from VC!").queue();
            } else {
                event.getChannel().sendMessage("That user is not in a voice channel.").queue();
            }
            return;
        }
        Member member = mentionedMembers.get(0);
        if (Objects.requireNonNull(member.getVoiceState()).inVoiceChannel())
        {
         event.getGuild().kickVoiceMember(member).queue();
         event.getChannel().sendMessage("Disconnected " + member.getAsMention() + " from VC!").queue();
        } else {
            event.getChannel().sendMessage("That user is not in a voice channel.").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Deafens a user from voice channel\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "vcdc";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"vcdisconnect","vckick","disconnectvc","dcvc"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
