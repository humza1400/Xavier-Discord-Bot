package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AntiRaidConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to view the anti-raid config.").build()).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Anti-Raid Config " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
        embed.addField("Kicks", "**1.** Operational: **`null`**\n **2.** Threshold **`null`**", true);
        embed.addField("Bans", "**3.** Operational: **`null`**\n **4.** Threshold **`null`**", true);
        embed.addField("Text-Channel Creations", "**5.** Operational: **`null`**\n **6.** Threshold **`null`**", true);
        embed.addField("Text-Channel Deletions", "**7.** Operational: **`null`**\n **8.** Threshold **`null`**", true);
        embed.addField("Voice-Chat Creations", "**9.** Operational: **`null`**\n **10.** Threshold **`null`**", true);
        embed.addField("Voice-Chat Deletions", "**11.** Operational: **`null`**\n **12.** Threshold **`null`**", true);
        embed.addField("Category Creations", "**13.** Operational: **`null`**\n **14.** Threshold **`null`**", true);
        embed.addField("Category Deletions", "**15.** Operational: **`null`**\n **16.** Threshold **`null`**", true);
        embed.addField("Role Creations", "**17.** Operational: **`null`**\n **18.** Threshold **`null`**", true);
        embed.addField("Role Deletions", "**19.** Operational: **`null`**\n **20.** Threshold **`null`**", true);
        embed.setFooter("Anti-Raid Config");
        embed.setTimestamp(Instant.now());
        embed.setColor(Core.getInstance().getColorTheme());
        embed.setDescription("Allocation: **`null`**");
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Returns the anti-raid config for the current guild\n`" + Core.PREFIX + getInvoke() + "`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n";
    }

    @Override
    public String getInvoke() {
        return "antiraidconfig";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"arcfg", "arconfig", "antiraidcfg", "cfg"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
