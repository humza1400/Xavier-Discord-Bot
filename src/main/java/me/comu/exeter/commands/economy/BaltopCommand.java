package me.comu.exeter.commands.economy;

import me.comu.exeter.commands.admin.WhitelistedJSONHandler;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class BaltopCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        StringBuilder stringBuffer = new StringBuilder();
        int counter2 = 1;
        for (String x : EconomyManager.getUsers().keySet()) {
            if (EconomyManager.getUsers().get(x).equals(event.getGuild().getId())) {
                User user = event.getJDA().getUserById(x);
                try {
                    String name = user.getName() + "#" + user.getDiscriminator();
                    stringBuffer.append("**" + counter2 + "**." + name + "\n");
                    counter2++;
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessage("The economy config contains an invalid user, please resolve this issue. (" + x + ")").queue();
                }
            }
        }
            event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + event.getGuild().getName() + " Top 10 Rich List:**\n" + stringBuffer.toString()).build()).queue();
            event.getChannel().sendMessage(stringBuffer.toString()).queue();
            WhitelistedJSONHandler.saveWhitelistConfig();
    }

    @Override
    public String getHelp() {
        return "Shows the richest people on the bot\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "baltop";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"richlist"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
