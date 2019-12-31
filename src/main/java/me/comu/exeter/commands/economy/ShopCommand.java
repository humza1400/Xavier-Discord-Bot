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
        embed.addField("Nitro Code", "**1.** Cost: **`null`**\n **2.** Stock: **`null`**", true);
        embed.addField("Custom Role", "**3.** Cost: **`null`**\n **4.** Stock: **`null`**", true);
        embed.addField("Hoisted Role", "**5.** Cost: **`null`**\n **6.** Stock: **`null`**", true);
        embed.addField("Custom VC", "**7.** Cost: **`null`**\n **8.** Stock: **`null`**", true);
        embed.addField("Custom Perms", "**9.** Cost: **`null`**\n **10.** Stock: **`null`**", true);
        embed.addField("Staff Role", "**11.** Cost: **`null`**\n **12** Stock: **`null`**", true);
//        embed.addField("Staff Role", "**** DM?: **`\u2705`**", true);
        embed.setFooter("Requested by " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.setDescription("Current Sale: **50%**");
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
        return new String[] {"market","marketplace"};
    }

     @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
