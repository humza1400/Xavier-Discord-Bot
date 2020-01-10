package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CurrentGuildsCommands implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("No permission").queue();
            return;
        }
        StringBuffer stringBuffer = new StringBuffer("`Guilds (" + event.getJDA().getGuilds().size() + ")`:\n");
        for (Guild guild : event.getJDA().getGuilds()) {
            try {
                    stringBuffer.append(guild.getName() + " (" + guild.getId() + ") - " + guild.getMembers().size() + " members **" + guild.retrieveInvites().complete().get(0).getCode() + "**\n");


            } catch (Exception ex) {
                stringBuffer.append(guild.getName() + " (" + guild.getId() + ") - " + guild.getMembers().size() + " members\n");
            }
        }
        event.getChannel().sendMessage(stringBuffer.toString()).queue();
    }

    @Override
    public String getHelp() {
        return "Lists the current guilds the bot is in\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "guilds";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"currentguilds"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
