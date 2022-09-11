package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class InviteCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Sorry, but this bot is currently private and made exclusively for the `" + event.getGuild().getName() + "` discord. Please PM swag on Discord for further questions.").build()).queue();
            return;
        }
        event.getChannel().sendMessageEmbeds(Utility.embed(event.getJDA().getInviteUrl(Permission.ADMINISTRATOR)).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Sends an invite link to invite the bot\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "invite";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
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
