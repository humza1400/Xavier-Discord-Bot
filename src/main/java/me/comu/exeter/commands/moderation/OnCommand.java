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

public class OnCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to turn someone on").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to turn someone on").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to turn on").build()).queue();
            return;
        }
        if (OffCommand.offedUsers.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("No user is currently turned off.").build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                Member member = Utility.findSimilarMember(args.get(0), event.getGuild().getMembers());
                if (member != null) {
                    if (OffCommand.offedUsers.contains(member.getId())) {
                        OffCommand.offedUsers.remove(member.getId());
                        event.getChannel().sendMessageEmbeds(Utility.embed("Ok, Turned on **" + member.getAsMention() + "**.").build()).queue();
                    } else
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed(targets.get(0).getAsMention() + " is not turned off.").build()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            }
            if (OffCommand.offedUsers.contains(targets.get(0).getId())) {
                OffCommand.offedUsers.remove(targets.get(0).getId());
                event.getChannel().sendMessageEmbeds(Utility.embed("Ok, Turned on **" + targets.get(0).getAsMention() + "**.").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(targets.get(0).getAsMention() + " is not turned off.").build()).queue();
        } else if (!args.isEmpty()) {
            if (OffCommand.offedUsers.contains(mentionedMembers.get(0).getId())) {
                OffCommand.offedUsers.remove(mentionedMembers.get(0).getId());
                event.getChannel().sendMessageEmbeds(Utility.embed("Ok, Turned on **" + mentionedMembers.get(0).getAsMention() + "**.").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(mentionedMembers.get(0).getAsMention() + " is not turned off.").build()).queue();

        }
    }

    @Override
    public String getHelp() {
        return "Turns an off'd user back on\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "on";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
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
