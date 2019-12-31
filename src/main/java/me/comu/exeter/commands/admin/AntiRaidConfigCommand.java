package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class AntiRaidConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("You don't have permission to ban that user").queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Anti-Raid Config " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
        embed.addField("Kicks & Bans", "**1.** Operational: **`null`**\n **2.** Range **`null`**", true);
        embed.addField("Channel Creations", "**3.** Operational: **`null`**\n **4.** Range **`null`**", true);
        embed.addField("Channel Deletions", "**5.** Operational: **`null`**\n **6.** Range **`null`**", true);
        embed.addField("Role Creations", "**7.** Operational: **`null`**\n **8.** Range **`null`**", true);
        embed.addField("Role Deletions", "**9.** Operational: **`null`**\n **10.** Range **`null`**", true);
        embed.addField("Mentions", "**11.** Operational: **`null`**", true);
        embed.addField("Whitelisted Users", "**** DM?: **`\u2705`**", true);
        embed.setFooter("Anti-Raid Config");
        embed.setTimestamp(Instant.now());
        embed.setDescription("Allocation: **`null`**");
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Returns the anti-raid config for the current guild\n`" + Core.PREFIX + getInvoke() + "`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n";
    }

    @Override
    public String getInvoke() {
        return "cfg";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"arcfg","arconfig","antiraidcfg","antiraidconfig"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
