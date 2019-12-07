package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MemberCountCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        String memberInfo = String.format(
                "**Total Members**: %s\n**Online Members**: %s\n**Offline Members**: %s\n**Bot Count**: %s",
                guild.getMemberCache().size(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus()  == OnlineStatus.ONLINE).count() + guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB).count() + guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.IDLE).count() ,
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.OFFLINE).count() + guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.INVISIBLE).count() ,
                guild.getMemberCache().stream().filter((m) -> m.getUser().isBot()).count());
        event.getChannel().sendMessage(EmbedUtils.embedMessage(memberInfo).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows the member count of the discord\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "membercount";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"usercount","mcount","ucount","mc"};
    }
}
