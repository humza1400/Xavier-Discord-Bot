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
import java.util.StringJoiner;

public class BanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();


        if (!Objects.requireNonNull(member).hasPermission(Permission.BAN_MEMBERS) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban users").build()).queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to ban users").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a valid user to ban").build()).queue();
            return;
        }
        String reason = String.join(" ", args.subList(1, args.size()));
        if (mentionedMembers.isEmpty()) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.forEach(stringJoiner::add);
            List<Member> targets = event.getGuild().getMembersByName(stringJoiner.toString(), true);
            if (targets.isEmpty()) {
                try {
                    Member member1 = event.getGuild().getMemberById(args.get(0));
                    if (member1 == null) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Sorry, I couldn't find that user.").build()).queue();
                        return;
                    }
                    if (args.size() > 1) {
                        if (!event.getGuild().getSelfMember().canInteract(Objects.requireNonNull(member1))) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                            return;
                        } else if (!Objects.requireNonNull(event.getMember()).canInteract(member1)) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();

                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user!").build()).queue();
                            return;
                        }
                        StringJoiner stringJoiner1 = new StringJoiner(" ");
                        args.stream().skip(1).forEach(stringJoiner1::add);
                        event.getGuild().ban(member1, 0, stringJoiner1.toString()).queue();
                        event.getChannel().sendMessageEmbeds(Utility.embed("Banned **" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** for `" + stringJoiner1 + "`").build()).queue();
                        return;
                    }
                    if (!event.getGuild().getSelfMember().canInteract(Objects.requireNonNull(member1))) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                        return;
                    } else if (!Objects.requireNonNull(event.getMember()).canInteract(member1)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user!").build()).queue();
                        return;
                    }

                    event.getGuild().ban(member1, 0).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed("Banned **" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "**").build()).queue();
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
                        event.getGuild().ban(member1, 0).queue();
                        event.getChannel().sendMessageEmbeds(Utility.embed("Banned **" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "**").build()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                    return;
                }
            }
            Member target = targets.get(0);
            if (reason.equals("")) {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                    return;

                }
                if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user").build()).queue();
                    return;
                }
                event.getGuild().ban(target, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Banned **%s**", Utility.removeMarkdown(target.getUser().getAsTag()))).build()).queue();
            } else {
                if (!selfMember.canInteract(target)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                    return;
                }
                if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user").build()).queue();
                    return;
                }
                event.getGuild().ban(target, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Banned **%s** for `%s`", Utility.removeMarkdown(target.getUser().getAsTag()), reason)).build()).queue();
            }
            return;
        }
        Member target = mentionedMembers.get(0);
        if (reason.equals("")) {
            if (!selfMember.canInteract(target)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                return;
            }
            if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user").build()).queue();
                return;
            }
            event.getGuild().ban(target, 0).reason(String.format("Banned by %#s", event.getAuthor())).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Banned %s", target.getUser().getAsTag())).build()).queue();
        } else {
            if (!selfMember.canInteract(target)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("My role is not high enough to ban that user!").build()).queue();
                return;
            }
            if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to ban that user").build()).queue();
                return;
            }
            event.getGuild().ban(target, 0).reason(String.format("Banned by %#s for %s", event.getAuthor(), reason)).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("Banned %s for `%s`", target.getUser().getAsTag(), reason)).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Bans the specified user\n" + "`" + Core.PREFIX + getInvoke() + " [user] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ban";
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
