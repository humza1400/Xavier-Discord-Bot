package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class AFKCommand implements ICommand {

    public static final HashMap<String, String> afkUsers = new HashMap<>();
    public static final HashMap<String, Integer> afkUserMessageIndex = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("clear") && event.getAuthor().getIdLong() == Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Cleared " + afkUsers.size() + " AFK users!").build()).queue();
            afkUsers.clear();
            afkUserMessageIndex.clear();
            return;
        }
        if (args.isEmpty() && afkUsers.containsKey(Objects.requireNonNull(event.getMember()).getId())) {
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getUser().getAsTag() + " is no longer AFK.").build()).queue();
            afkUsers.remove(event.getMember().getId());
            afkUserMessageIndex.remove(event.getMember().getId());
            return;
        }
        if (afkUsers.containsKey(Objects.requireNonNull(event.getMember()).getId()) && afkUserMessageIndex.get(event.getMember().getId()) == 3) {
            afkUsers.remove(event.getMember().getId());
            afkUserMessageIndex.remove(event.getMember().getId());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getUser().getAsTag() + " is no longer AFK.").build()).queue();
        } else {
            if (args.isEmpty()) {
                afkUsers.put(event.getMember().getId(), null);
                afkUserMessageIndex.put(event.getMember().getId(), 0);
                event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " is now AFK.").build()).queue();
            } else {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.forEach(stringJoiner::add);
                if (stringJoiner.toString().contains(".gg/")) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("You can't put invite links as your AFK-Message").build()).queue();
                    return;
                }
                afkUsers.put(event.getMember().getId(), stringJoiner.toString());
                afkUserMessageIndex.put(event.getMember().getId(), 0);
                event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " is now AFK: " + Utility.removeMentions(stringJoiner.toString())).build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
