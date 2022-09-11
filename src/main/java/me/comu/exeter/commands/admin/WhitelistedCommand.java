package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WhitelistedCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to see the whitelisted users.").build()).queue();
            return;
        }
        if (WhitelistCommand.getWhitelistedIDs().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Nobody is currently whitelisted.").build()).queue();
            return;
        }
        if (event.getAuthor().getIdLong() == Core.OWNERID && !args.isEmpty() && args.get(0).equals("-g")) {
            StringBuilder globalStringBuffer = new StringBuilder();
            int counter = 0;
            for (WhitelistKey x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                User user = event.getJDA().getUserById(x.getUserID());
                try {
                    String level = WhitelistCommand.getWhitelistedIDs().get(x);
                    String name = Utility.removeMarkdown(Objects.requireNonNull(user).getAsTag()) + " - " + level + String.format(" (%s)", Objects.requireNonNull(event.getJDA().getGuildById(x.getGuildID())).getName());
                    globalStringBuffer.append(" + ").append(name).append("\n");
                    counter++;
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("The whitelist config contained an invalid user and was automatically resolved. (" + x.getUserID() + ")").build()).queue();
                    WhitelistCommand.getWhitelistedIDs().remove(x);
                    Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
                }
            }
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(MarkdownUtil.bold(counter + " Whitelisted Users: (GLOBAL)\n" + globalStringBuffer)).setColor(Core.getInstance().getColorTheme()).build()).queue();
            Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
            return;
        }
        StringBuilder stringBuffer = new StringBuilder();
        int counter2 = 0;

        for (WhitelistKey x : WhitelistCommand.getWhitelistedIDs().keySet()) {
            if (x.getGuildID().equals(event.getGuild().getId())) {
                User user = event.getJDA().getUserById(x.getUserID());
                try {
                    String level = WhitelistCommand.getWhitelistedIDs().get(x);
                    String name = Utility.removeMarkdown(Objects.requireNonNull(user).getAsTag()) + " - " + level;
                    stringBuffer.append(" + ").append(name).append("\n");
                    counter2++;
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("The whitelist config contained an invalid user and was automatically resolved. (" + x.getUserID() + ")").build()).queue();
                    WhitelistCommand.getWhitelistedIDs().remove(x);
                    Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());

                }
            }
        }

        event.getChannel().sendMessageEmbeds(Utility.embedMessage(MarkdownUtil.bold(counter2 + " Whitelisted Users: (LOCAL)\n" + stringBuffer)).setColor(Core.getInstance().getColorTheme()).build()).queue();
        Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());

    }

    @Override
    public String getHelp() {
        return "See all the users on the whitelist\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "whitelisted";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"seewhitelist", "arwhitelisted", "whitelistlist", "trustlist", "wld"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
