package me.comu.exeter.commands.owner;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class b4uLoggerCommand implements ICommand{

    public static String server = null;
    public static boolean enabled = false;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }

        if (args.isEmpty()) {
            server = event.getGuild().getId();
            event.getChannel().sendMessageEmbeds(Utility.embed("Server set to " + event.getGuild().getName()).build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("on")) {
            if (server == null) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Server isn't set").build()).queue();
            } else {
                enabled = true;
                event.getChannel().sendMessageEmbeds(Utility.embed("b4u Logger Enabled").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("off")) {
            if (server == null) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Server isn't set").build()).queue();
            } else {
                enabled = false;
                event.getChannel().sendMessageEmbeds(Utility.embed("b4u Logger Disabled").build()).queue();
            }
        }



    }

    @Override
    public String getHelp() {
        return "Private b4u logger command\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "b4ulogger";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
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
