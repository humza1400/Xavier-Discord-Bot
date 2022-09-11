package me.comu.exeter.commands.owner;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class AuthorizeCommand implements ICommand {

    public static List<String> authorized = new ArrayList<>();


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            if (authorized.contains(event.getGuild().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Removed authorization for **" + Utility.removeMentionsAndMarkdown(event.getGuild().getName()) + "**.").build()).queue();
                authorized.remove(event.getGuild().getId());
            } else {
                authorized.add(event.getGuild().getId());
                event.getChannel().sendMessageEmbeds(Utility.embed("Authorized **" + Utility.removeMentionsAndMarkdown(event.getGuild().getName()) + "**.").build()).queue();
            }
        } else {
            if (args.get(0).equalsIgnoreCase("list"))
            {
                StringBuilder buffer = new StringBuilder();
                for (String string : authorized) {
                    buffer.append(" (+) ").append(string).append("\n");
                }
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Authorized Guilds (" + authorized.size() + ")\n").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now());
                ArrayList<Page> pages = new ArrayList<>();
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setContent(buffer.toString());
                Queue<Message> messages = messageBuilder.buildAll(MessageBuilder.SplitPolicy.ANYWHERE);
                for (Message message : messages) {
                    embedBuilder.setDescription(message.getContentRaw());
                    pages.add(new Page(PageType.EMBED, embedBuilder.build()));
                }
                event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
                return;
            }
            try {
                Guild guild = event.getJDA().getGuildById(args.get(0));
                if (guild == null) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("**Couldn't Authorize!** `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "` is an invalid guild.").build()).queue();
                    return;
                }
                if (authorized.contains(guild.getId())) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("Removed authorization for **" + Utility.removeMentionsAndMarkdown(guild.getName()) + "**.").build()).queue();
                    authorized.remove(event.getGuild().getId());
                } else {
                    authorized.add(guild.getId());
                    event.getChannel().sendMessageEmbeds(Utility.embed("Authorized **" + Utility.removeMentionsAndMarkdown(guild.getName()) + "**.").build()).queue();
                }
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a valid guild-id").build()).queue();
            }
        }
        Core.getInstance().saveConfig(Core.getInstance().getAuthorizedJSONHandler());
    }

    public static List<String> getAuthorized() {
        return authorized;
    }

    public static void setAuthorized(List<String> authorized) {
        AuthorizeCommand.authorized = authorized;
    }

    @Override
    public String getHelp() {
        return "Authorizes the specified guild to premium features\n`" + Core.PREFIX + getInvoke() + " [guild]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "authorize";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"auth", "authority", "addpremium", "givepremium", "addauth"};
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
