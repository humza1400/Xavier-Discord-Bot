package me.comu.exeter.events;

import me.comu.exeter.commands.bot.UsernameHistoryCommand;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;

public class UsernamePresenceUpdateEvent extends ListenerAdapter {

    @Override
    public void onUserUpdateName(@Nonnull UserUpdateNameEvent event) {
        if (UsernameHistoryCommand.usernames.containsKey(event.getUser().getId())) {
            UsernameHistoryCommand.usernames.get(event.getUser().getId()).put(UsernameHistoryCommand.dtf.format(Instant.now()), event.getNewValue() + "#" +event.getUser().getDiscriminator());
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(UsernameHistoryCommand.dtf.format(Instant.now()), event.getOldValue() + "#" + event.getUser().getDiscriminator());
            hashMap.put(UsernameHistoryCommand.dtf.format(Instant.now()), event.getNewValue() + "#" + event.getUser().getDiscriminator());
            UsernameHistoryCommand.usernames.put(event.getUser().getId(), hashMap);
        }
    }

    @Override
    public void onUserUpdateDiscriminator(@Nonnull UserUpdateDiscriminatorEvent event) {
        if (UsernameHistoryCommand.usernames.containsKey(event.getUser().getId())) {
            UsernameHistoryCommand.usernames.get(event.getUser().getId()).put(UsernameHistoryCommand.dtf.format(Instant.now()), event.getUser().getName() + "#" + event.getNewValue());
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(UsernameHistoryCommand.dtf.format(Instant.now()), event.getUser().getName() + "#" + event.getOldValue());
            hashMap.put(UsernameHistoryCommand.dtf.format(Instant.now()), event.getUser().getName() + "#" + event.getNewValue());
            UsernameHistoryCommand.usernames.put(event.getUser().getId(), hashMap);
        }
    }
}
