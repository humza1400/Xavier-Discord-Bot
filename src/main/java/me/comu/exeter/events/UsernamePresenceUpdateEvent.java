package me.comu.exeter.events;

import me.comu.exeter.commands.bot.UsernameHistoryCommand;
import me.comu.exeter.commands.moderation.StatusRoleCommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;

public class UsernamePresenceUpdateEvent extends ListenerAdapter {

    @Override
    public void onUserUpdateName(@Nonnull UserUpdateNameEvent event) {
        if (UsernameHistoryCommand.usernames.containsKey(event.getUser().getId())) {
            UsernameHistoryCommand.usernames.get(event.getUser().getId()).put(Utility.dtf.format(Instant.now()), event.getNewValue() + "#" + event.getUser().getDiscriminator());
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(Utility.dtf.format(Instant.now()), event.getOldValue() + "#" + event.getUser().getDiscriminator());
            hashMap.put(Utility.dtf.format(Instant.now()), event.getNewValue() + "#" + event.getUser().getDiscriminator());
            UsernameHistoryCommand.usernames.put(event.getUser().getId(), hashMap);
        }
    }

    @Override
    public void onUserUpdateDiscriminator(@Nonnull UserUpdateDiscriminatorEvent event) {
        if (UsernameHistoryCommand.usernames.containsKey(event.getUser().getId())) {
            UsernameHistoryCommand.usernames.get(event.getUser().getId()).put(Utility.dtf.format(Instant.now()), event.getUser().getName() + "#" + event.getNewValue());
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(Utility.dtf.format(Instant.now()), event.getUser().getName() + "#" + event.getOldValue());
            hashMap.put(Utility.dtf.format(Instant.now()), event.getUser().getName() + "#" + event.getNewValue());
            UsernameHistoryCommand.usernames.put(event.getUser().getId(), hashMap);
        }
    }

    @Override
    public void onUserUpdateActivities(@NotNull UserUpdateActivitiesEvent event) {
        if (event.getMember().getUser().isBot() || event.getNewValue() == null || StatusRoleCommand.roleId == null || StatusRoleCommand.guildId == null || StatusRoleCommand.message.isEmpty() || !event.getGuild().getId().equals(StatusRoleCommand.guildId) || event.getMember().getActivities().isEmpty()) {
            return;
        }
        boolean found = false;
        Role role = event.getGuild().getRoleById(StatusRoleCommand.roleId);
        if (role == null) {
            return;
        }
        for (Activity activity : event.getNewValue()) {
            if (activity.getType() == Activity.ActivityType.CUSTOM_STATUS) {
                for (String string : StatusRoleCommand.message) {
                    if (activity.getName().toLowerCase(Locale.ROOT).contains(string.toLowerCase(Locale.ROOT))) {
                        found = true;
                        StatusRoleCommand.members.add(event.getMember().getId());
                        event.getGuild().addRoleToMember(event.getMember(), role).reason("Status Update").queue();
                    }
                }
                if (!found && StatusRoleCommand.members.contains(event.getMember().getId())) {
                    StatusRoleCommand.members.remove(event.getMember().getId());
                    event.getGuild().removeRoleFromMember(event.getMember(), role).reason("Removed Status Update").queue();
                }
            }
        }
    }

}
