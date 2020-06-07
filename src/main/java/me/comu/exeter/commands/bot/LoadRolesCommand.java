package me.comu.exeter.commands.bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoadRolesCommand implements ICommand {

    private EventWaiter eventWaiter;

    public LoadRolesCommand(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessage("You don't have permission to load roles").queue();
            return;
        }
        if (!Objects.requireNonNull(event.getGuild().getSelfMember()).hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessage("I don't have permission to load roles").queue();
            return;
        }
        if (!CopyRolesCommand.copied) {
            event.getChannel().sendMessage("No roles are copied").queue();
            return;
        }

//        event.getChannel().sendMessage(new EmbedBuilder().setTitle("\u26A0 Warning").setDescription("Are you sure you want to load this backup? **All roles the bot can interact with will get deleted and reconstructed from the backup!**").setColor(Color.YELLOW).build()).queue((message -> message.addReaction("\u2705").queue((success -> initWaiter(event.getAuthor().getId(), message.getId(), event.getChannel().getId(), event.getJDA(), event)))));
        loadRoles(event);
    }

    private void loadRoles(GuildMessageReceivedEvent event) {

        CopyRolesCommand.copied = false;
        event.getGuild().getRoles().forEach(role -> {
            if (event.getGuild().getSelfMember().canInteract(role) && !role.isManaged() && !role.isPublicRole()) {
                role.delete().queue();
            }
        });
        CopyRolesCommand.roles.forEach(restorableRole -> event.getGuild().createRole().setName(restorableRole.getName()).setColor(restorableRole.getColor()).setMentionable(restorableRole.isMentionable()).setHoisted(restorableRole.isHoisted()).setPermissions(restorableRole.getPermissions()).queue());
        event.getChannel().sendMessage("Successfully loaded `" + CopyRolesCommand.roles.size() + "` roles.").queue();
        CopyRolesCommand.clearCopiedRoles(CopyRolesCommand.roles);

    }

    private void initWaiter(String author, String message, String channel, JDA jda, GuildMessageReceivedEvent msgRecieveEvent) {
        eventWaiter.waitForEvent(GuildMessageReactionAddEvent.class, (event) -> {
            MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
            User user = event.getUser();
            return !user.isBot() && author.equals(user.getId()) && event.getMessageId().equals(message) && reactionEmote.getEmoji().equals("\u2705");
        }, (event) -> {
            TextChannel textChannel = event.getJDA().getTextChannelById(channel);
            try {
                Objects.requireNonNull(textChannel).retrieveMessageById(message).queue(message1 -> message1.editMessage(event.getUser().getAsMention() + " has loaded in a backup!").override(true).queue());
                loadRoles(msgRecieveEvent);
            } catch (NullPointerException ex) {
                Objects.requireNonNull(textChannel).sendMessage("Something went wrong, try again!").queue();
            }
        }, 10, TimeUnit.SECONDS, () -> {
            TextChannel textChannel = jda.getTextChannelById(channel);
            try {
                Objects.requireNonNull(textChannel).retrieveMessageById(message).queue(message1 -> {
                    message1.editMessage("Backup was never confirmed, role load cancelled.").override(true).queue();
                    message1.removeReaction("\u2705").queue();
                    message1.addReaction("\u274C").queue();
                });

            } catch (NullPointerException ex) {
                Objects.requireNonNull(textChannel).sendMessage("Something went wrong, try again!").queue();
            }
        });
    }

    @Override
    public String getHelp() {
        return "Loads the copied roles  into your server\n `" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`\n**Warning:** This deletes all current roles the bot can interact with";
    }

    @Override
    public String getInvoke() {
        return "loadroles";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"rolesload", "roleload"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
