package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class TagCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a tag or check the tags: `" + Core.PREFIX + getInvoke() + " tag-list`.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("tag-list")) {
            if (CreateTagCommand.tags.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No tags have been set. Set one by doing `" + Core.PREFIX + "createtag [tag] <content>`.").build()).queue();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder("__All Tags:__\n");
            CreateTagCommand.tags.forEach((k, v) -> stringBuilder.append("`").append(k).append("`: ").append(v).append("\n"));
            event.getChannel().sendMessageEmbeds(Utility.embed(stringBuilder.toString()).build()).queue();
            return;
        }
        if (CreateTagCommand.tags.containsKey(args.get(0)))
            event.getChannel().sendMessageEmbeds(Utility.embed(CreateTagCommand.tags.get(args.get(0))).build()).queue();
        else
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + Utility.removeMentions(args.get(0)) + "` does not exist as a tag!").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
