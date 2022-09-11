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

public class DisconnectUserCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.VOICE_MOVE_OTHERS) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to disconnect a user from VC").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.VOICE_MOVE_OTHERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to disconnect that user").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to disconnect from VC").build()).queue();
            return;
        }
        if (mentionedMembers.isEmpty())
        {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            } 
            if (Objects.requireNonNull(targets.get(0).getVoiceState()).inVoiceChannel())
            {
                event.getGuild().kickVoiceMember(targets.get(0)).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed("Disconnected " + targets.get(0).getAsMention() + " from VC!").build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not in a voice channel.").build()).queue();
            }
            return;
        }
        Member member = mentionedMembers.get(0);
        if (Objects.requireNonNull(member.getVoiceState()).inVoiceChannel())
        {
         event.getGuild().kickVoiceMember(member).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Disconnected " + member.getAsMention() + " from VC!").build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not in a voice channel.").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
