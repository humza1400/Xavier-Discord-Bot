package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CurrentGuildsCommands implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (event.getAuthor().getIdLong() != Core.OWNERID && !event.getAuthor().getId().equalsIgnoreCase("210956619788320768")) {
            event.getChannel().sendMessage("No permission").queue();
            return;
        }
        StringBuilder stringBuffer = new StringBuilder("`Guilds (" + event.getJDA().getGuilds().size() + ")`:\n");
        for (Guild guild : event.getJDA().getGuilds()) {
            try {
                    stringBuffer.append(guild.getName()).append(" (").append(guild.getId()).append(") - ").append(guild.getMembers().size()).append(" members **").append(guild.retrieveInvites().complete().get(0).getCode()).append("**\n");

            } catch (Exception ex)
            {
                if (!event.getGuild().getSelfMember().hasPermission(Permission.CREATE_INSTANT_INVITE))
                stringBuffer.append(guild.getName()).append(" (").append(guild.getId()).append(") - ").append(guild.getMembers().size()).append(" members **").append(guild.getTextChannels().get(0).createInvite().setMaxAge(0).complete().getCode()).append("**\n");
                else
                stringBuffer.append(guild.getName()).append(" (").append(guild.getId()).append(") - ").append(guild.getMembers().size()).append(" members\n");
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
