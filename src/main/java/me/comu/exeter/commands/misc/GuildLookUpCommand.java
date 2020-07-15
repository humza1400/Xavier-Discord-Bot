package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GuildLookUpCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify an ID to look up!").queue();
            return;
        }
        try {
            event.getChannel().sendMessage(EmbedUtils.embedImage(Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(args.get(0))).getIconUrl()).concat("?size=256&f=.gif")).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(args.get(0) + " belongs to `" + Objects.requireNonNull(event.getJDA().getGuildById(args.get(0))).getName() + "`").build()).queue();

        } catch (NumberFormatException | NullPointerException ex) {
            event.getChannel().sendMessage("No guild exists with that ID in my database.").queue();
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
}
