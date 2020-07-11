package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class AutoResponseCommand implements ICommand {

    public static final HashMap<String, String> responses = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert an auto-response message with content").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear") && (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("Successfully cleared all auto-responses (" + responses.size() + ")").queue();
            responses.clear();
            return;
        }
        if (args.get(0).equalsIgnoreCase("response-list")) {
            if (responses.isEmpty()) {
                event.getChannel().sendMessage("No auto-responses have been set. Set one by doing `" + Core.PREFIX + "autoresponse [content]`.").queue();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder("__All Auto-Responses:__\n");
            responses.forEach((k, v) -> stringBuilder.append("`").append(k).append("`: ").append(v).append("\n"));
            event.getChannel().sendMessage(stringBuilder.toString()).queue();
            return;
        }

        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("You can't ping people in auto-respnoses, sorry.").queue();
            return;
        }
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMessage().getContentRaw().contains(".gg/")) {
            event.getChannel().sendMessage("Only admins can create auto-responses with discord invites").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        if (stringJoiner.toString().isEmpty())
        {
            event.getChannel().sendMessage("You need to specify content to go alongside the auto-response").queue();
            return;
        }
        String tag = args.get(0);
        if (responses.containsKey(tag)) {
            event.getChannel().sendMessage("`" + Utility.removeMentions(args.get(0)) + "` already exists as an auto-response!").queue();
            return;
        }
        String tagMessage = Utility.removeMentions(stringJoiner.toString());
        responses.put(tag, tagMessage);
        event.getChannel().sendMessage("Successfully added `" + tag + "` with an auto-response of `" + tagMessage + "`").queue();


    }

    @Override
    public String getHelp() {
        return "Auto-Replies to the message\n `" + Core.PREFIX + getInvoke() + " [message] <content>/[response-list]/[clear]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "autoresponse";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"autoresponseadd", "addautoresponse", "addar", "autoreply", "autoreplyadd"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
