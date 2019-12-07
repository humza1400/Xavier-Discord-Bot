package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WarnCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("You don't have permission to warn someone").queue();
            return;
        }

            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessage("Please specify a valid user to warn").queue();
                return;
            }
            if (args.size() == 1) {
                Member target = mentionedMembers.get(0);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0xFFDE53);
                embedBuilder.setTitle("⚠Warning⚠");
                embedBuilder.setDescription("Warned " + target.getAsMention());
                embedBuilder.setFooter("Warned by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
                embedBuilder.setTimestamp(Instant.now());
                List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
                event.getChannel().deleteMessages(messages2).queueAfter(3, TimeUnit.SECONDS);
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
            else  {
                    Member target = mentionedMembers.get(0);
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0xFFDE53);
                    embedBuilder.setTitle("⚠Warning⚠");
                    int subIndex = Core.PREFIX.length() + 4 + target.getAsMention().length();
                    String reason = message.substring(subIndex, message.length());
                    embedBuilder.setDescription("Warned " + target.getAsMention() + " for `" + reason + "`");
                    embedBuilder.setFooter("Warned by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
                    embedBuilder.setTimestamp(Instant.now());
                List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
                event.getChannel().deleteMessages(messages2).queueAfter(3, TimeUnit.MILLISECONDS);
                    event.getChannel().sendMessage(embedBuilder.build()).queue();

            }


    }

    @Override
    public String getHelp() {
        return "Warns the specific user\n`" + Core.PREFIX + getInvoke() + " [user] <reason>`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "warn";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
