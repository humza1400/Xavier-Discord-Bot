package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class StatusCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS))
            event.getChannel().sendMessage(new EmbedBuilder().addField("Bot Status", Core.jda.getStatus().name(), false).addField("Discord API", Wrapper.getDiscordStatus(), false).setColor(Wrapper.getAmbientColor()).build()).queue();
        else
            event.getChannel().sendMessage("Bot Status: `" + Core.jda.getStatus().name() + "` Discord API: `" + Wrapper.getDiscordStatus() + "`.").queue();
    }

    @Override
    public String getHelp() {
        return "Shows the status of the Discord API and the Bot API\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "status";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"apistatus", "botstatus", "discordstatus"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
