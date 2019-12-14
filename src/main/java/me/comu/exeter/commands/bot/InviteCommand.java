package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class InviteCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Sorry, but this bot is currently private and made exclusively for the lovestruck discord. Please PM swag#3231 for further questions.").queue();
    }

    @Override
    public String getHelp() {
        return "Sends an invite link to invite the bot\n`" + Core.PREFIX + getInvoke() + "`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "invite";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
