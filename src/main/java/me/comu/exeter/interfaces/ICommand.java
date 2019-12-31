package me.comu.exeter.interfaces;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public interface ICommand {

    void handle(List<String> args, GuildMessageReceivedEvent event);
    String getHelp();
    String getInvoke();
    String[] getAlias();
    Category getCategory();

    enum Category {ADMIN, MODERATION, BOT, MUSIC, ECONOMY, MARRIAGE, MISC, NUKE}


}
