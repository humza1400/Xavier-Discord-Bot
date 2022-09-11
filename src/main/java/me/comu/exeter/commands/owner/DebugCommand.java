package me.comu.exeter.commands.owner;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DebugCommand implements ICommand {

    public static Map<String, String[]> debug = new HashMap<>();


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Specify an error-id.").build()).queue();
            return;
        }

        if (debug.containsKey(args.get(0))) {
            String[] values = debug.get(args.get(0));
            if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Core.getInstance().getColorTheme())
                        .setTitle("Stacktrace #" + Utility.removeMentionsAndMarkdown(args.get(0)))
                        .setFooter("Report Generated at " + values[2])
                        .setDescription("**Guild:** `" + values[1] + "`\n**Command Executor:** `" + values[0] + "`\n**Command:** `" + Utility.removeMentionsAndMarkdown(values[3]) + "`\n" + MarkdownUtil.codeblock(values[4]))
                        .build()).queue();
            } else {

                event.getChannel().sendMessage("Report Generated at " + values[2] +
                        "\n\nGuild: " + values[1] +
                        "\nCommand Executor: " + values[0]
                        + "\nCommand: " + values[3] + "\n"
                        + MarkdownUtil.codeblock(values[4])).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("clear")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Cleared **" + debug.size() + "** error logs").build()).queue();
            debug.clear();
        } else if (args.size() > 1 && (args.get(0).equalsIgnoreCase("delete") || args.get(0).equalsIgnoreCase("del") || args.get(0).equalsIgnoreCase("remove") || args.get(0).equalsIgnoreCase("rem"))) {
            if (debug.containsKey(args.get(1))) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully deleted **" + args.get(1) + "**.").build()).queue();
                debug.remove(args.get(1));
            } else {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't delete that error-log because it doesn't exist!").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("list")) {
            if (debug.size() == 0) {
                event.getChannel().sendMessageEmbeds(Utility.embed("There are **" + debug.size() + "** Error-Logs").build()).queue();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            debug.keySet().forEach(msg -> stringBuilder.append("(+) ").append(msg).append("\n"));
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Error Logs (" + debug.size() + ")\n").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now());
            ArrayList<Page> pages = new ArrayList<>();
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setContent(stringBuilder.toString());
            Queue<Message> messages = messageBuilder.buildAll(MessageBuilder.SplitPolicy.ANYWHERE);
            for (Message message : messages) {
                embedBuilder.setDescription(message.getContentRaw());
                pages.add(new Page(PageType.EMBED, embedBuilder.build()));
            }
            event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No error report has been made with that id.").build()).queue();
        }


    }


    @Override
    public String getHelp() {
        return "Debugs a specified stacktrace exception\n`" + Core.PREFIX + getInvoke() + " [stacktrace-id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "debug";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"stacktrace", "error"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
