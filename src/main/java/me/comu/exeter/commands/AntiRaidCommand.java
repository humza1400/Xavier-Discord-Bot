package me.comu.exeter.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AntiRaidCommand implements ICommand {

    private static boolean active = true;
    private EventWaiter eventWaiter;

    public AntiRaidCommand(EventWaiter waiter)
    {
        this.eventWaiter = waiter;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(getHelp()).queue();
            return;
        }
        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to toggle anti-raid").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!active) {
                active = true;
                event.getChannel().sendMessage("Anti-Raid is now active").queue();
            } else
                event.getChannel().sendMessage("Anti-Raid is already enabled").queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (active) {
                active = false;
                event.getChannel().sendMessage("Anti-Raid is no longer active").queue();
            } else
                event.getChannel().sendMessage("Anti-Raid is already disabled").queue();
        }
            eventWaiter.waitForEvent(GuildBanEvent.class, (e) -> !e.getUser().isFake(), e -> {
                event.getChannel().sendMessage((String.format("(%s)[%s]<%#s>: %s", event.getGuild().getName(), e.getGuild().getChannels().get(4), event.getAuthor(), e.getUser()))).queue();
            });
            eventWaiter.waitForEvent(MessageReceivedEvent.class, (e) -> e.isFromType(ChannelType.TEXT), e-> {
                event.getChannel().sendMessage((String.format("(%s)[%s]<%#s>: %s", event.getGuild().getName(), BindLogChannelCommand.logChannelID, event.getAuthor(), e.getMessage()))).queue();
            });

    }


    public static boolean isActive()
    {
        return active;
    }

    @Override
    public String getHelp() {
        return "Tries to prevent a malicious attack against the server\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "antiraid";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"ar","anti-raid","antir","noraid"};
    }
}
