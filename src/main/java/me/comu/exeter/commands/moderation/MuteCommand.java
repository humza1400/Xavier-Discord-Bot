package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MuteCommand implements ICommand {

    public static List<String> mutedUsers = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!Objects.requireNonNull(member).hasPermission(Permission.MESSAGE_MANAGE) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to mute users").build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to mute that user").build()).queue();
            return;
        }
        if (!SetMuteRoleCommand.isMuteRoleSet(event.getGuild())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please set a mute-role first `" + Core.PREFIX + "help muterole`").build()).queue();
            return;
        }

        if (!Objects.requireNonNull(event.getMember()).canInteract(Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId()))))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to access the mute role").build()).queue();
            return;
        }
        if (!event.getGuild().getSelfMember().canInteract(Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId()))))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I can't interact with the mute role!").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a user to mute").build()).queue();
            return;
        }
        if (mentionedMembers.isEmpty()) {
            try {
                List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
                if (targets.isEmpty()) {
                    Member delchiemerMember = Utility.findSimilarMember(args.get(0), event.getGuild().getMembers());
                    if (delchiemerMember != null) {
                        try {
                            if (delchiemerMember.getRoles().contains(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))) {
                                event.getChannel().sendMessageEmbeds(Utility.embed("They're already muted bro, let it go").build()).queue();
                                return;
                            }
                            event.getGuild().addRoleToMember(delchiemerMember, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason(String.format("Muted by %#s", event.getAuthor())).queue();
                            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Muted %s.", delchiemerMember.getAsMention())).build()).queue();
                            mutedUsers.add(delchiemerMember.getId());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong and I was too lazy to debug this lol").build()).queue();
                        }
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                    return;
                }
                Member target = targets.get(0);
                String reason = String.join(" ", args.subList(1, args.size()));
                if (target.getRoles().contains(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("They're already muted bro, let it go").build()).queue();
                    return;
                }
                if (reason.equals("")) {
                    event.getGuild().addRoleToMember(target, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason(String.format("Muted by %#s", event.getAuthor())).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Muted %s", target.getAsMention())).build()).queue();
                    mutedUsers.add(target.getId());
                } else {
                    event.getGuild().addRoleToMember(target, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason(String.format("Muted by %#s for %s", event.getAuthor(), reason)).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Muted %s for `%s`", target.getAsMention(), reason)).build()).queue();
                    mutedUsers.add(target.getId());
                }
            } catch (HierarchyException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot mute anyone whilst the mute role is at a higher precedent than my own").build()).queue();
            }
            return;
        }
        Member target = mentionedMembers.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (target.getRoles().contains(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))) {
            event.getChannel().sendMessageEmbeds(Utility.embed("They're already muted bro, let it go").build()).queue();
            return;
        }
        try {
            if (reason.equals("")) {
                event.getGuild().addRoleToMember(target, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason(String.format("Muted by %#s", event.getAuthor())).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Muted %s", target.getAsMention())).build()).queue();
                mutedUsers.add(target.getId());
            } else {
                event.getGuild().addRoleToMember(target, Objects.requireNonNull(event.getGuild().getRoleById(SetMuteRoleCommand.getMutedRoleMap().get(event.getGuild().getId())))).reason(String.format("Muted by %#s for %s", event.getAuthor(), reason)).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Muted %s for `%s`", target.getAsMention(), reason)).build()).queue();
                mutedUsers.add(target.getId());
            }
        } catch (HierarchyException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I cannot mute anyone whilst the mute role is at a higher precedent than my own").build()).queue();
        } catch (IllegalArgumentException exx) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Provided Role is not part of this Guild").build()).queue();
        }


    }

    @Override
    public String getHelp() {
        return "Mutes the specified user from interacting with voice/text channels\n`" + Core.PREFIX + getInvoke() + " [reason]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "mute";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"silence", "quiet", "stfu", "shhh"};
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
