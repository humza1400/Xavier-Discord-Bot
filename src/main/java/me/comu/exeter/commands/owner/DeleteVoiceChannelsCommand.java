package me.comu.exeter.commands.owner;

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
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        List<VoiceChannel> voiceChannels = event.getGuild().getVoiceChannels();
        int tcSize = voiceChannels.size();

        try {
            for (int i = 0; i <= tcSize; i++) {
                try {
                    voiceChannels.get(i).delete().queue();
                } catch (HierarchyException | IndexOutOfBoundsException ignored) {

                }
            }
        } catch (HierarchyException | ErrorResponseException | IndexOutOfBoundsException ignored) {

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

    @Override
    public boolean isPremium() {
        return false;
    }
}
