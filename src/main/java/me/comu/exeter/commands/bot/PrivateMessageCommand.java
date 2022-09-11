package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class PrivateMessageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to private message anyone.").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a user to private message.").build()).queue();
            return;
        }
        if (mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0)) + ".").build()).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Multiple users found! Try mentioning the user instead.").build()).queue();
                return;
            }
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            event.getMessage().delete().queue();
            sendPrivateMessage(targets.get(0).getUser(), stringJoiner.toString(), event.getChannel());
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        event.getMessage().delete().queue();
        sendPrivateMessage(mentionedMembers.get(0).getUser(), stringJoiner.toString(), event.getChannel());
    }

    private void sendPrivateMessage(User user, String content, TextChannel textChannel) {
        user.openPrivateChannel().queue((channel) ->
        {
            try {
                channel.sendMessage(content).queue((success) -> textChannel.sendMessageEmbeds(Utility.embedMessage("Successfully messaged " + user.getAsMention()).setColor(Core.getInstance().getColorTheme()).build()).queue()
                , (error) ->
                        textChannel.sendMessageEmbeds(Utility.errorEmbed("Couldn't message " + user.getAsMention()).build()).queue());
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

    @Override
    public boolean isPremium() {
        return false;
    }

}