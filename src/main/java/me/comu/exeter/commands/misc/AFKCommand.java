package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class AFKCommand implements ICommand {

    public static HashMap<String, String> afkUsers = new HashMap<>();
    public static HashMap<String, Integer> afkUserMessageIndex = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("clear") && event.getAuthor().getIdLong() == Core.OWNERID) {
            event.getChannel().sendMessage("Cleared " + afkUsers.size() + " AFK users!").queue();
            afkUsers.clear();
            afkUserMessageIndex.clear();
            return;
        }
        if (args.isEmpty() && afkUsers.containsKey(Objects.requireNonNull(event.getMember()).getId())) {
            event.getChannel().sendMessage(event.getMember().getUser().getAsTag() + " is no longer AFK.").queue();
            afkUsers.remove(event.getMember().getId());
            afkUserMessageIndex.remove(event.getMember().getId());
            return;
        }
        if (afkUsers.containsKey(Objects.requireNonNull(event.getMember()).getId()) && afkUserMessageIndex.get(event.getMember().getId()) == 3) {
            afkUsers.remove(event.getMember().getId());
            afkUserMessageIndex.remove(event.getMember().getId());
            event.getChannel().sendMessage(event.getMember().getUser().getAsTag() + " is no longer AFK.").queue();
        } else {
            if (args.isEmpty()) {
                afkUsers.put(event.getMember().getId(), null);
                afkUserMessageIndex.put(event.getMember().getId(), 0);
                event.getChannel().sendMessage(event.getMember().getAsMention() + " is now AFK.").queue();
            } else {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.forEach(stringJoiner::add);
                afkUsers.put(event.getMember().getId(), stringJoiner.toString());
                afkUserMessageIndex.put(event.getMember().getId(), 0);
                event.getChannel().sendMessage(event.getMember().getAsMention() + " is now AFK: " + stringJoiner.toString().replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Sets your account AFK\n`" + Core.PREFIX + getInvoke() + " [AFK-Message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "afk";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setafk", "goafk", "afkstatus", "brb"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
