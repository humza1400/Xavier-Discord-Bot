package me.comu.exeter.events;

import me.comu.exeter.commands.economy.EconomyManager;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Random;

public class VoiceChannelCreditsEvent extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        if (event.getMember().getUser().isBot())
            return;
        double d = Math.random();
        int amount = new Random().nextInt(51);
        if (d > 0.85)
        {
            if (EconomyManager.verifyUser(event.getMember().getId()))
                EconomyManager.getUsers().put(event.getMember().getId(), 0);
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + amount);
        }
    }
}
