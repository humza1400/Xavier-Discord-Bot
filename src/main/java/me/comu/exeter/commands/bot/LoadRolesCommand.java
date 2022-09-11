package me.comu.exeter.commands.bot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
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

    private final EventWaiter eventWaiter;

    public LoadRolesCommand(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && event.getMember().getIdLong() != event.getGuild().getOwnerIdLong()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to load roles.").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(event.getGuild().getSelfMember()).hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permission to load roles.").build()).queue();
            return;
        }
        if (!CopyRolesCommand.copied) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No roles are copied.").build()).queue();
            return;
        }

        event.getChannel().sendMessageEmbeds(new EmbedBuilder().setTitle("\u26A0 Warning").setDescription("Are you sure you want to load this backup? **All roles the bot can interact with will get deleted and reconstructed from the backup!**").setColor(Core.getInstance().getColorTheme()).build()).queue((message -> message.addReaction("checkmark:959654268250488892").queue((success -> {
            initWaiter(event.getAuthor().getId(), message.getId(), event.getChannel().getId(), event.getJDA(), event);
        }))));

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
            return !user.isBot() && author.equals(user.getId()) && event.getMessageId().equals(message) && reactionEmote.getEmote().getId().equals("959654268250488892");
        }, (event) -> {
            TextChannel textChannel = event.getJDA().getTextChannelById(channel);
            try {
                Objects.requireNonNull(textChannel).retrieveMessageById(message).queue(message1 -> message1.editMessageEmbeds(Utility.embed(event.getUser().getAsMention() + " has loaded in a backup!").build()).override(true).queue());
                loadRoles(msgRecieveEvent);
            } catch (NullPointerException ex) {
                Objects.requireNonNull(textChannel).sendMessageEmbeds(Utility.errorEmbed("Something went wrong, try again!").build()).queue();
            }
        }, 10, TimeUnit.SECONDS, () -> {
            TextChannel textChannel = jda.getTextChannelById(channel);
            try {
                Objects.requireNonNull(textChannel).retrieveMessageById(message).queue(message1 -> {
                    message1.editMessageEmbeds(Utility.embed("Backup was never confirmed, role load cancelled.").build()).override(true).queue();
                    message1.removeReaction("checkmark:959654268250488892").queue();
                    message1.addReaction("no:959656234108190760").queue();
                });

            } catch (NullPointerException ex) {
                Objects.requireNonNull(textChannel).sendMessageEmbeds(Utility.errorEmbed("Something went wrong, try again!").build()).queue();
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

    @Override
    public boolean isPremium() {
        return true;
    }
}
