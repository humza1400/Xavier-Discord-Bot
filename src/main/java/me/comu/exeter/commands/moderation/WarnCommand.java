package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WarnCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            event.getChannel().sendMessage("You don't have permission to warn someone").queue();
            return;
        }

            if (args.isEmpty()) {
                event.getChannel().sendMessage("Please specify a valid user to warn").queue();
                return;
            }
            if (mentionedMembers.isEmpty()) {
                List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
                if (targets.isEmpty()) {
                    event.getChannel().sendMessage("Couldn't find the user " + Utility.removeMentions(args.get(0))).queue();
                    return;
                } else if (targets.size() > 1) {
                    event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                    return;
                }
                if (args.size() == 1) {
                    Member target = targets.get(0);
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0xFFDE53);
                    embedBuilder.setTitle(":warning:Warning:warning:");
                    embedBuilder.setDescription("Warned " + target.getAsMention());
                    embedBuilder.setFooter("Warned by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                    embedBuilder.setTimestamp(Instant.now());
                    event.getMessage().delete().queueAfter(3, TimeUnit.MILLISECONDS);
                    event.getChannel().sendMessage(embedBuilder.build()).queue();
                } else {
                    Member target = targets.get(0);
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0xFFDE53);
                    embedBuilder.setTitle(":warning:Warning:warning:");
                    int subIndex = Core.PREFIX.length() + 5;
                    String reason = message.substring(subIndex).replaceFirst(args.get(0), "");
                    embedBuilder.setDescription("Warned " + target.getAsMention() + " for `" + reason + "`");
                    embedBuilder.setFooter("Warned by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                    embedBuilder.setTimestamp(Instant.now());
                    event.getMessage().delete().queueAfter(3, TimeUnit.MILLISECONDS);
                    event.getChannel().sendMessage(embedBuilder.build()).queue();
                }
                return;
            }
            if (args.size() == 1) {
                Member target = mentionedMembers.get(0);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0xFFDE53);
                embedBuilder.setTitle(":warning:Warning:warning:");
                embedBuilder.setDescription("Warned " + target.getAsMention());
                embedBuilder.setFooter("Warned by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                embedBuilder.setTimestamp(Instant.now());
                event.getMessage().delete().queueAfter(3, TimeUnit.MILLISECONDS);
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
            else  {
                    Member target = mentionedMembers.get(0);
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0xFFDE53);
                    embedBuilder.setTitle(":warning:Warning:warning:");
                    int subIndex = Core.PREFIX.length() + 5 + target.getAsMention().length();
                    String reason = message.substring(subIndex);
                    embedBuilder.setDescription("Warned " + target.getAsMention() + " for `" + reason + "`");
                    embedBuilder.setFooter("Warned by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                    embedBuilder.setTimestamp(Instant.now());
                event.getMessage().delete().queueAfter(3, TimeUnit.MILLISECONDS);
                    event.getChannel().sendMessage(embedBuilder.build()).queue();


            }


    }

    @Override
    public String getHelp() {
        return "Warns the specific user\n`" + Core.PREFIX + getInvoke() + " [user] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "warn";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
