package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsernameHistoryCommand implements ICommand {

    public static final HashMap<String, HashMap<String, String>> usernames = new HashMap<>();


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please mention a valid user or specify a user-id\nOur current database consists of **" + usernames.size() + "** users").build()).queue();
            return;
        }
        if (event.getAuthor().getIdLong() == Core.OWNERID && args.get(0).equalsIgnoreCase("-logall")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("`Logging all names...`").build()).queue();
            logAllNames(event.getJDA());
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear") && event.getAuthor().getIdLong() == Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Cleared " + usernames.size() + " logged usernames!").build()).queue();
            usernames.clear();
            Core.getInstance().saveConfig(Core.getInstance().getUsernameHistoryHandler());
            return;
        }
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            try {
                event.getJDA().retrieveUserById(args.get(0)).queue(user -> {
                    if (!usernames.containsKey(args.get(0))) {
                        event.getChannel().sendMessageEmbeds(Utility.embed("I have no logged history of " + user.getAsTag() + ".").build()).queue();
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(Utility.dtf.format(Instant.now()), user.getAsTag());
                        usernames.put(user.getId(), hashMap);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder("Note: This is just what the bot has gathered locally and is not complete\n");
                        HashMap<String, String> hashMap = usernames.get(args.get(0));
                        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                            String lol = entry.getKey() + ": " + Utility.removeMarkdown(entry.getValue());
                            stringBuilder.append(MarkdownUtil.codeblock(lol));
                        }
                        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                                .setTitle(user.getAsTag() + "'s Name History (" + user.getId() + ")")
                                .setColor(Core.getInstance().getColorTheme())
                                .setDescription(stringBuilder.toString())
                                .setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                                .build()).queue();
                    }
                }, failure -> event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No user exists in my cache with that ID.").build()).queue());
            } catch (Exception ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No user exists in my cache with that ID").build()).queue();
            }
        } else {
            User user = event.getMessage().getMentionedMembers().get(0).getUser();
            if (!usernames.containsKey(user.getId())) {
                event.getChannel().sendMessage("I have no logged history of " + user.getAsTag()).queue();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Utility.dtf.format(Instant.now()), user.getAsTag());
                usernames.put(user.getId(), hashMap);
            } else {
                StringBuilder stringBuilder = new StringBuilder("Note: This is just what the bot has gathered locally and is not complete\n");
                HashMap<String, String> hashMap = usernames.get(user.getId());
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    String lol = entry.getKey() + ": " + Utility.removeMarkdown(entry.getValue());
                    stringBuilder.append(MarkdownUtil.codeblock(lol));
                }
                event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setTitle(user.getAsTag() + "'s Name History (" + user.getId() + ")")
                        .setColor(Core.getInstance().getColorTheme())
                        .setDescription(stringBuilder.toString())
                        .setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl())
                        .build()).queue();
            }
        }
        Core.getInstance().saveConfig(Core.getInstance().getUsernameHistoryHandler());

    }

    public static void setUsernames(Map<String, HashMap<String, String>> map) {
        usernames.clear();
        usernames.putAll(map);
    }

    public static void logAllNames(JDA jda) {
        for (Guild guild : jda.getGuilds()) {
            System.out.println("loading guild: " +guild.getName());
            guild.loadMembers(member -> {
                System.out.println("loading " + member.getUser().getAsTag());
                if (!usernames.containsKey(member.getId())) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(Utility.dtf.format(Instant.now()), member.getUser().getAsTag());
                    usernames.put(member.getId(), hashMap);
                    System.out.println("Added " + member.getUser().getAsTag());
                }
            });
        }
        Core.getInstance().saveConfig(Core.getInstance().getUsernameHistoryHandler());
    }

    @Override
    public String getHelp() {
        return "Returns all past usernames of a user in our database\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "usernamehistory";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"history", "namehistory", "unhistory", "usernames", "username", "un", "uns", "unh","uname"};
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
