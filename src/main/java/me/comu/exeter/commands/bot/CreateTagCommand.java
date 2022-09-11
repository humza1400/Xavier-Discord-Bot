package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class CreateTagCommand implements ICommand {

    static final HashMap<String, String> tags = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid tag and message.").build()).queue();
            return;
        }
        if (args.size() == 1) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a message to go alongside the tag.").build()).queue();
            return;
        }
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't ping people in tags, sorry.").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMessage().getContentRaw().contains(".gg/")) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Only admins can create commands with discord invites.").build()).queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        String tag = args.get(0);
        if (tags.containsKey(tag)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + Utility.removeMentions(args.get(0)) + "` already exists as a tag!").build()).queue();
            return;
        }
        String tagMessage = Utility.removeMentions(stringJoiner.toString());
        tags.put(tag, tagMessage);
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully added `" + tag + "` with the content of `" + tagMessage + "`.").build()).queue();

    }

    @Override
    public String getHelp() {
        return "Assigns a custom message to a tag\n`" + Core.PREFIX + getInvoke() + " [tag] <content>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "createtag";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"tagcreate"};
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
