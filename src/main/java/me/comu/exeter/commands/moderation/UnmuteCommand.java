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

public class UnmuteCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a valid user to unmute").build()).queue();
            return;
        }

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_ROLES) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to unmute users").build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to unmute that user").build()).queue();
            return;
        }
        if (mentionedMembers.isEmpty() && !args.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                Member delchiemerMember = Utility.findSimilarMember(args.get(0), event.getGuild().getMembers());
                if (delchiemerMember != null) {
                    try {
                        if (delchiemerMember.getRoles().contains(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))) {
                            event.getGuild().removeRoleFromMember(delchiemerMember, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).queue();
                            event.getChannel().sendMessageEmbeds(Utility.embed("Unmuted " + delchiemerMember.getAsMention()).build()).queue();
                        } else {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not muted").build()).queue();
                        }
                    } catch (Exception ex) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong and I was too lazy to debug this lol").build()).queue();
                    }
                    return;
                }
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            }
            try {
                if (event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())) == null) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Looks like the previous mute role was deleted, please set a new one.").build()).queue();
                    SetMuteRoleCommand.getMutedRoleMap().remove(event.getGuild().getId());
                    return;
                }
            } catch (NullPointerException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Looks like the previous mute role was deleted, please set a new one.").build()).queue();
                SetMuteRoleCommand.getMutedRoleMap().remove(event.getGuild().getId());
                return;
            }
            Member target = targets.get(0);
            if (target.getRoles().contains(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))) {
                event.getGuild().removeRoleFromMember(target, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed("Unmuted " + target.getAsMention()).build()).queue();
            } else {

                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not muted").build()).queue();
            }
            return;
        }
        if (!SetMuteRoleCommand.getMutedRoleMap().containsKey(event.getGuild().getId())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No mute role is set, set one by doing `" + Core.PREFIX + "setmuterole <role>`").build()).queue();
            return;
        }
        Member target = mentionedMembers.get(0);
        if (target.getRoles().contains(Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId()))))) {
            event.getGuild().removeRoleFromMember(target, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Unmuted " + target.getAsMention()).build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("That user is not muted").build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Unmutes a muted user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "unmute";
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
