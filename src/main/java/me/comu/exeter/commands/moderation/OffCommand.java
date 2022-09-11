package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OffCommand implements ICommand {

    public static final List<String> offedUsers = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to turn someone off").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to turn someone off").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to turn off").build()).queue();
            return;
        }
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                Member member = Utility.findSimilarMember(args.get(0), event.getGuild().getMembers());
                if (member != null) {
                    if (offedUsers.contains(member.getId())) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed(member.getAsMention() + " is already turned off.").build()).queue();
                        return;
                    }
                    if (member.getId().equals(event.getJDA().getSelfUser().getId())) {
                        event.getChannel().sendMessageEmbeds(Utility.embed("You can't turn me off :(").build()).queue();
                        return;
                    }
                    offedUsers.add(member.getId());
                    event.getChannel().sendMessageEmbeds(Utility.embed("Ok, Turned off **" + member.getAsMention() + "**.").build()).queue();
                }
                return;
            }
            if (offedUsers.contains(targets.get(0).getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(targets.get(0).getAsMention() + " is already turned off.").build()).queue();
                return;
            }
            if (targets.get(0).getId().equals(event.getJDA().getSelfUser().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You can't turn me off :(").build()).queue();
                return;
            }
            offedUsers.add(targets.get(0).getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("Ok, Turned off **" + targets.get(0).getAsMention() + "**.").build()).queue();
        } else if (!args.isEmpty()) {
            if (offedUsers.contains(mentionedMembers.get(0).getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(mentionedMembers.get(0).getAsMention() + " that user is already turned off.").build()).queue();
                return;
            }
            offedUsers.add(mentionedMembers.get(0).getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("Ok, Turned off **" + mentionedMembers.get(0).getAsMention() + "**.").build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Deletes all messages the off'd user tries to send\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "off";
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
