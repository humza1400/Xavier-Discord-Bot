package me.comu.exeter.commands.owner;


import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class RestartCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission.").build()).queue();
            return;
        }
        long time = System.currentTimeMillis();
        String channel = event.getChannel().getId();
        AtomicLong atomicLong = new AtomicLong();
        event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 **Restarting...**").build()).queue(success -> atomicLong.set(success.getIdLong()));
        Core.getInstance().initialize();
        Objects.requireNonNull(Core.getInstance().getJDA().getTextChannelById(channel)).retrieveMessageById(atomicLong.get()).queue(msg ->
                msg.editMessageEmbeds(Utility.embed("<a:checkmark:959654268250488892> Successfully rebooted, took **" + (System.currentTimeMillis() - time) + " ms**.").build()).queue()
        );
    }


    @Override
    public String getHelp() {
        return "Restarts the bot\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "restart";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"restart()", "reboot", "reinitialize", "reboot()", "reinitialize()", "reinit()", "reinit"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}

