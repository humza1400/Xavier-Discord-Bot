package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class PurgeEmojisCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }

            for (Emote emote : event.getGuild().getEmotes()) {
                try {
                    emote.delete().queue();
                } catch (HierarchyException ex1) {
                }
            }


    }

    @Override
    public String getHelp() {
        return "Deletes all emojis\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "delemojis";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"delemotes","purgeemotes","deleteemojis","purgeemojis"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
