package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class KickCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        String reason = String.join(" ", args.subList(1, args.size()));
        if (!Objects.requireNonNull(member).hasPermission(Permission.KICK_MEMBERS) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick someone").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a valid user to kick").build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to kick that user").build()).queue();
            return;
        }
        if (mentionedMembers.isEmpty() && !args.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.stream().skip(1).forEach(stringJoiner::add);
                try {
                    Member member1 = event.getGuild().getMemberById(args.get(0));
                    if (member1 == null) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Sorry, I couldn't find that user.").build()).queue();
                        return;
                    }
                    if (args.size() > 1) {
                        if (!event.getGuild().getSelfMember().canInteract(Objects.requireNonNull(member1))) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to kick that user!").build()).queue();
                            return;
                        } else if (!Objects.requireNonNull(event.getMember()).canInteract(member1)) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick that user!").build()).queue();
                            return;
                        }
                        event.getGuild().kick(Objects.requireNonNull(member1), args.get(0)).queue();
                        event.getChannel().sendMessageEmbeds(Utility.embed("Kicked **" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** for `" + stringJoiner + "`").build()).queue();
                        return;
                    }
                    if (!event.getGuild().getSelfMember().canInteract(Objects.requireNonNull(member1))) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to kick that user!").build()).queue();
                        return;
                    } else if (!Objects.requireNonNull(event.getMember()).canInteract(member1)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick that user!").build()).queue();
                        return;
                    }
                    event.getGuild().kick(Objects.requireNonNull(member1)).queue(null, failure -> event.getChannel().sendMessageEmbeds(Utility.embed("You don't have permission to kick that user").build()).queue());
                    event.getChannel().sendMessageEmbeds(Utility.embed("Kicked **" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "**").build()).queue();
                    return;
                } catch (NumberFormatException ex) {
                    Member member1 = Utility.findSimilarMember(args.get(0), event.getGuild().getMembers());
                    if (member1 != null) {
                        if (!event.getGuild().getSelfMember().canInteract(Objects.requireNonNull(member1))) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                            return;
                        } else if (!Objects.requireNonNull(event.getMember()).canInteract(member1)) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user!").build()).queue();
                            return;
                        }
                        event.getGuild().kick(member1).queue();
                        event.getChannel().sendMessageEmbeds(Utility.embed("Kicked **" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "**").build()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                    return;
                }
            }
            Member target = targets.get(0);
            if (reason.equals("")) {
                try {
                    if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick that user").build()).queue();
                        return;
                    }
                    event.getGuild().kick(target, String.format("Kicked by %#s", event.getAuthor())).queue();
                } catch (HierarchyException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to kick that user!").build()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Kicked **%s**", Utility.removeMarkdown(target.getUser().getAsTag()))).build()).queue();
            } else {
                try {
                    if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick that user").build()).queue();
                        return;
                    }
                    event.getGuild().kick(target, String.format("Kicked by %#s for `%s`", event.getAuthor(), reason)).queue();
                } catch (HierarchyException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to kick that user!").build()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Kicked **%s** for `%s`", Utility.removeMarkdown(target.getUser().getAsTag()), reason)).build()).queue();
            }
            return;
        }
        Member target = mentionedMembers.get(0);
        if (reason.equals("")) {
            try {
                if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick that user").build()).queue();
                    return;
                }
                event.getGuild().kick(target, String.format("Kicked by %#s", event.getAuthor())).queue();
            } catch (HierarchyException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to kick that user!").build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Kicked **%s**", Utility.removeMarkdown(target.getUser().getAsTag()))).build()).queue();
        } else {
            try {
                if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to kick that user").build()).queue();
                    return;
                }
                event.getGuild().kick(target, String.format("Kicked by %#s for %s", event.getAuthor(), reason)).queue();
            } catch (HierarchyException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to kick that user!").build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Kicked **%s** for %s", Utility.removeMarkdown(target.getUser().getAsTag()), reason)).build()).queue();
        }


    }

    @Override
    public String getHelp() {
        return "Kicks the specified user\n" + "`" + Core.PREFIX + getInvoke() + " [user] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kick";
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
