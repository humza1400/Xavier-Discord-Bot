package me.comu.exeter.events;

import me.comu.exeter.commands.bot.UsernameHistoryCommand;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;

public class UsernamePresenceUpdateEvent extends ListenerAdapter {

    @Override
    public void onUserUpdateName(@Nonnull UserUpdateNameEvent event) {
        if (UsernameHistoryCommand.usernames.containsKey(event.getUser().getId())) {
            UsernameHistoryCommand.usernames.get(event.getUser().getId()).put(Instant.now().toString(), event.getNewValue());
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("null", event.getOldValue());
            hashMap.put(Instant.now().toString(), event.getNewValue());
            UsernameHistoryCommand.usernames.put(event.getUser().getId(), hashMap);
        }
    }
}
