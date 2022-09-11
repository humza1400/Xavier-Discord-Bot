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
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert an auto-response message with content.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear") && (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully cleared all auto-responses (" + responses.size() + ").").build()).queue();
            responses.clear();
            return;
        }
        if (args.get(0).equalsIgnoreCase("response-list")) {
            if (responses.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No auto-responses have been set. Set one by doing `" + Core.PREFIX + "autoresponse [content]`.").build()).queue();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder("__All Auto-Responses:__\n");
            responses.forEach((k, v) -> stringBuilder.append("`").append(k).append("`: ").append(v).append("\n"));
            event.getChannel().sendMessageEmbeds(Utility.embed(stringBuilder.toString()).build()).queue();
            return;
        }

        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't ping people in auto-respnoses, sorry.").build()).queue();
            return;
        }
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMessage().getContentRaw().contains(".gg/")) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Only admins can create auto-responses with discord invites.").build()).queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        if (stringJoiner.toString().isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to specify content to go alongside the auto-response.").build()).queue();
            return;
        }
        String tag = args.get(0);
        if (responses.containsKey(tag)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("`" + Utility.removeMentions(args.get(0)) + "` already exists as an auto-response!").build()).queue();
            return;
        }
        String tagMessage = Utility.removeMentions(stringJoiner.toString());
        responses.put(tag, tagMessage);
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully added `" + tag + "` with an auto-response of `" + tagMessage + "`.").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
