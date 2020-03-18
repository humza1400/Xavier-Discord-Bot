package me.comu.exeter.commands.bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReactionRoleCommand implements ICommand {
    private EventWaiter waiter;

    public ReactionRoleCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to create reaction roles").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permissions to create reaction roles").queue();
            return;
        }
        if (args.isEmpty() || args.size() < 3)
        {
            event.getChannel().sendMessage("Please insert a valid: message-id, role-id, and emoji (in that order).").queue();
            return;
        }

        try {
            event.getChannel().retrieveMessageById(args.get(0)).queue(message -> {
                event.getChannel().sendMessage("Set reaction role message successfully!").queue();
                startWaiter(message.getIdLong(), event.getChannel().getIdLong(), args.get(2), event.getGuild().getRoleById(args.get(1)), event.getGuild());
            });
        } catch (Exception ex)
        {
            event.getChannel().sendMessage("Caught exception. Please insert a valid: message-id, role-id, and emoji (in that order).").queue();
        }


    }
    private void startWaiter(long messageId, long channelId, String emojiID, Role role, Guild guild) {
        waiter.waitForEvent(
                GuildMessageReactionAddEvent.class,
                (event) -> {
                    MessageReaction.ReactionEmote emote = event.getReactionEmote();
                    User user = event.getUser();
                    return !user.isBot() && event.getMessageIdLong() == messageId && !emote.isEmote();
                },
                (event) -> {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    User user = event.getUser();
                    guild.addRoleToMember(guild.getMemberById(user.getId()), role).queue();
                    channel.sendMessageFormat("%#s role reacted and received **%s**!", user, role.getName()).queue();
                },
                1, TimeUnit.MINUTES,
                () -> {
                    TextChannel channel = guild.getTextChannelById(channelId);
                    channel.sendMessage("I stopped listening for reactions").queue();
                }
        );
    }

    @Override
    public String getHelp() {
        return "Creates a reaction role with a message\n`" + Core.PREFIX + getInvoke() + " [message-id] <role-id> (emoji-id)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "reactionrole";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"rr", "rradd", "reactionroleadd"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
