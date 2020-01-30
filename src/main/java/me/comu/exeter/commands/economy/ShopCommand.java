package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.Products;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class ShopCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        NumberFormat myFormat = NumberFormat.getInstance();

        embed.setTitle(String.format("%s Marketplace", event.getGuild().getName()));
        embed.addField("Nitro Code", "**1.** Cost: **`" + myFormat.format(Products.NITRO_CODE) + "`**\n **2.** Stock: **`" + Products.NITRO_CODE_STOCK + "`**\n **3.** ID: **`" + Products.NITRO_CODE_ID + "`**", true);
        embed.addField("Custom Role", "**4.** Cost: **`" + myFormat.format(Products.CUSTOM_ROLE) + "`**\n **5.** Stock: **`\u221E`**\n **6.** ID: **`" + Products.CUSTOM_ROLE_ID + "`**", true);
        embed.addField("Hoisted Role", "**7.** Cost: **`" + myFormat.format(Products.HOISTED_ROLE) + "`**\n **8.** Stock: **`\u221E`**\n **9.** ID: **`" + Products.HOISTED_ROLE_ID + "`**", true);
        embed.addField("Custom VC", "**10.** Cost: **`" + myFormat.format(Products.CUSTOM_VC) + "`**\n **11.** Stock: **`\u221E`**\n **12.** ID: **`" + Products.CUSTOM_VC_ID + "`**", true);
        embed.addField("Custom Perms", "**13.** Cost: **`" + myFormat.format(Products.VC_PERMS) + "`**\n **14.** Stock: **`\u221E`**\n **15.** ID: **`" + Products.VC_PERMS_ID + "`**", true);
        embed.addField("Staff Role", "**16.** Cost: **`" + myFormat.format(Products.STAFF_ROLE) + "`**\n **17** Stock: **`\u221E`**\n **18.** ID: **`" + Products.STAFF_ROLE_ID+ "`**", true);
//        embed.addField("Staff Role", "**** DM?: **`\u2705`**", true);
        embed.setFooter("Requested by " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.setDescription("Current Sale: **~~" + Products.SALE + "%~~**");
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
        return new String[]{"market", "marketplace"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
