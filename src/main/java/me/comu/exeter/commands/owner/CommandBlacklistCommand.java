package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class CommandBlacklistCommand implements ICommand {

    private static final List<String> commandBlacklist = new ArrayList<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        if (args.isEmpty() || event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please mention a user to command-blacklist").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("list")) {
            if (commandBlacklist.size() == 0) {
                event.getChannel().sendMessageEmbeds(Utility.embed("There are **no** users command-blacklisted.").build()).queue();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            commandBlacklist.forEach(msg -> stringBuilder.append("(+) ").append(msg).append("\n"));
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("There are **" + commandBlacklist.size() + "** users command-blacklisted.\n" + stringBuilder).setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now());
            ArrayList<Page> pages = new ArrayList<>();
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setContent(stringBuilder.toString());
            Queue<Message> messages = messageBuilder.buildAll(MessageBuilder.SplitPolicy.ANYWHERE);
            for (Message message : messages) {
                embedBuilder.setDescription(message.getContentRaw());
                pages.add(new Page(PageType.EMBED, embedBuilder.build()));
            }
            event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Cleared **" + commandBlacklist.size() + "** users from the command blacklist!").build()).queue();
            commandBlacklist.clear();
            Core.getInstance().saveConfig(Core.getInstance().getCmdBlacklistJSONHandler());
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);
        if (member.getIdLong() == Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't command-blacklist the owner of the bot!").build()).queue();
            return;
        }
        if (commandBlacklist.contains(member.getId())) {
            commandBlacklist.remove(member.getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("Removed **" + Utility.removeMarkdown(member.getUser().getAsTag()) + "** from the command-blacklist.").build()).queue();
            Core.getInstance().saveConfig(Core.getInstance().getCmdBlacklistJSONHandler());
        } else {
            commandBlacklist.add(member.getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("Added **" + Utility.removeMarkdown(member.getUser().getAsTag()) + "** to the command-blacklist").build()).queue();
            Core.getInstance().saveConfig(Core.getInstance().getCmdBlacklistJSONHandler());
        }
    }

    public static List<String> getCommandBlacklist() {
        return commandBlacklist;
    }

    public static void setCommandBlacklist(List<String> list) {
        commandBlacklist.clear();
        commandBlacklist.addAll(list);
    }

    @Override
    public String getHelp() {
        return "Blacklists the specified users from using commands\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "commandblacklist";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"cblacklist", "cmdblacklist", "cmdsblacklist", "cbl", "cmdbl"};
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
