package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class GameCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to change the streaming status.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("stop"))
        {
            Core.getInstance().getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        Core.getInstance().getJDA().getPresence().setActivity(Activity.playing(stringJoiner.toString()).asRichPresence());
        event.getChannel().sendMessageEmbeds(Utility.embed("Now playing " + stringJoiner + ".").build()).queue();

    }


    @Override
    public String getHelp() {
        return "Sets the playing status for the bot\n" + "`" + Core.PREFIX + getInvoke() + " [status]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "playing";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}


