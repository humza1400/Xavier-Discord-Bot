package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class ShopCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(String.format("%s Marketplace", event.getGuild().getName()));
        embed.addField("Weapons", "**1.** Operational: **`null`**\n **2.** Range **`null`**", true);
        embed.addField("Food", "**3.** Operational: **`null`**\n **4.** Range **`null`**", true);
        embed.addField("Clothes", "**5.** Operational: **`null`**\n **6.** Range **`null`**", true);
        embed.addField("idk yet", "**7.** Operational: **`null`**\n **8.** Range **`null`**", true);
        embed.addField("idk yet", "**9.** Operational: **`null`**\n **10.** Range **`null`**", true);
        embed.addField("idk yet", "**11.** Operational: **`null`**", true);
        embed.addField("idk yet", "**** DM?: **`\u2705`**", true);
        embed.setFooter("Requested by " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.setDescription("Current Sale: **`50%`**");
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows the shop UI where you can purchase items\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "shop";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"marketplace"};
    }
}
