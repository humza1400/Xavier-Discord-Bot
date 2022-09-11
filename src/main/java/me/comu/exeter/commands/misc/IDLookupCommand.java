package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class IDLookupCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify an ID to look up!").build()).queue();
            return;
        }
        try {
            event.getJDA().retrieveUserById(args.get(0)).queue((user -> event.getChannel().sendMessageEmbeds(Utility.embedImage(user.getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).setTitle(args.get(0) + " belongs to `" + user.getAsTag() + "` " + user.getAsMention()).build()).queue()), (error) ->
                    event.getChannel().sendMessageEmbeds(Utility.embed("No user found with that ID").build()).queue());

        } catch (NumberFormatException | NullPointerException ex) {
            event.getChannel().sendMessageEmbeds(Utility.embed("No user exists with that ID.").build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Gets the user associated with the specified ID\n`" + Core.PREFIX + getInvoke() + "[id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "idlookup";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"lookup", "id", "whosid", "whoid"};
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
