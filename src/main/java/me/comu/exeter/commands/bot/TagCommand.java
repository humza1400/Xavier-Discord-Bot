package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TagCommand implements ICommand {

    static HashMap<String, String> tags = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a tag or check the tags: `" + Core.PREFIX + getInvoke() + " tag-list`.").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("tag-list")) {
            if (CreateTagCommand.tags.isEmpty()) {
                event.getChannel().sendMessage("No tags have been set. Set one by doing `" + Core.PREFIX + "createtag [tag] <content>`.").queue();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder("__All Tags:__\n");
            CreateTagCommand.tags.forEach((k, v) -> {
                stringBuilder.append("`").append(k).append("`: ").append(v).append("\n");
            });
            event.getChannel().sendMessage(stringBuilder.toString()).queue();
            return;
        }
        if (CreateTagCommand.tags.containsKey(args.get(0)))
            event.getChannel().sendMessage(CreateTagCommand.tags.get(args.get(0))).queue();
        else
            event.getChannel().sendMessage("`" + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere") + "` does not exist as a tag!").queue();
    }

    @Override
    public String getHelp() {
        return "Returns the tag content of the specified tag\n`" + Core.PREFIX + getInvoke() + " [tag]/[tag-list] `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "tag";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"gettag"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
