package me.comu.exeter.events;

import me.comu.exeter.commands.economy.EconomyManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Random;

public class CreditOnMessageEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        double d = Math.random();
        int amount = new Random().nextInt(31);
        if (d > 0.97 && amount != 0)
        {
            event.getChannel().sendMessage(String.format("%s got lucky and received `%s` credits!", event.getAuthor().getName(), amount)).queue();
            if (EconomyManager.verifyUser(event.getAuthor().getId()))
                EconomyManager.getUsers().put(event.getAuthor().getId(), 0);
            EconomyManager.setBalance(event.getAuthor().getId(), EconomyManager.getBalance(event.getAuthor().getId()) + amount);
        }

    }
}
