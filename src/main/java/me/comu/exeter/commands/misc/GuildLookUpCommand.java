package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GuildLookUpCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify an ID to look up!").build()).queue();
            return;
        }
        try {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(args.get(0))).getIconUrl()).concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).setTitle(args.get(0) + " belongs to `" + Objects.requireNonNull(event.getJDA().getGuildById(args.get(0))).getName() + "`").build()).queue();

        } catch (NumberFormatException | NullPointerException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No guild exists with that ID in my database.").build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Gets the guild associated with the specified ID\n`" + Core.PREFIX + getInvoke() + " [id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "glookup";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"guildlookup", "gid", "serverlookup", "whoguild", "serverid", "guildid"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
