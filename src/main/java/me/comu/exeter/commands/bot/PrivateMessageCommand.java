package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrivateMessageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("You don't have permission to private message someone").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a user to private message").queue();
            return;
        }
        if (mentionedMembers.isEmpty() && !args.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone","everyone").replaceAll("@here","here")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            int subIndex = Core.PREFIX.length() + getInvoke().length() + targets.get(0).getAsMention().length() + 1;
            String postMessage = message.substring(subIndex);
            event.getMessage().delete().queue();
            sendPrivateMessage(targets.get(0).getUser(), postMessage.replaceFirst(args.get(0), ""));
            event.getChannel().sendMessage(EmbedUtils.embedMessage("Successfully messaged " + args.get(0)).build()).queue();
            return;
        }
        int subIndex = Core.PREFIX.length() + getInvoke().length() + mentionedMembers.get(0).getAsMention().length() + 1;
        String postMessage = message.substring(subIndex);
        event.getMessage().delete().queue();
        sendPrivateMessage(mentionedMembers.get(0).getUser(), postMessage);
        event.getChannel().sendMessage(EmbedUtils.embedMessage("Successfully messaged " + mentionedMembers.get(0).getEffectiveName()).build()).queue();
    }

    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            try {
                channel.sendMessage(content).queue();
            } catch (Exception e) {
                Logger.getLogger().print("Couldn't message " + user.getName() + "#" + user.getDiscriminator());
            }
        });
    }

    @Override
    public String getHelp() {
        return "Private messages the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dm";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"pm"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

}