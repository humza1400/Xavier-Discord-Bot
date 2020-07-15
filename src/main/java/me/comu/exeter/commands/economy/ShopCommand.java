package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.util.Products;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ShopCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (event.getAuthor().getIdLong() == Core.OWNERID && args.size() == 2 && args.get(0).equalsIgnoreCase("sale")) {
            try {
                Products.SALE = Integer.parseInt(args.get(1));
                event.getChannel().sendMessage("Set the marketplace sale to " + Products.SALE + "%").queue();
                return;
            } catch (Exception ex) {
                event.getChannel().sendMessage("Invalid sale price number!").queue();
                return;
            }
        }

        EmbedBuilder embed = new EmbedBuilder();
        ArrayList<Page> pages = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                embed.clear();
                NumberFormat myFormat = NumberFormat.getInstance();
                embed.setTitle(String.format("%s Marketplace", event.getGuild().getName()));
                embed.addField("Protection", "Cost: **`" + myFormat.format(Products.PROTECTION) + "`**\nID: **`" + Products.PROTECTION_ID + "`**" + "\n*" + Products.PROTECTION_INFO + "*", false);
                embed.addField("Pistol", "Cost: **`" + myFormat.format(Products.GLOCK) + "`**\nID: **`" + Products.GLOCK_ID + "`**\n*" + Products.GLOCK_INFO + "*", false);
                embed.addField("Draco", "Cost: **`" + myFormat.format(Products.DRACO) + "`**\nID: **`" + Products.DRACO_ID + "`**\n*" + Products.DRACO_INFO + "*", false);
                embed.addField("Ammo", "Cost: **`" + myFormat.format(Products.AMMO) + "`**\nID: **`" + Products.AMMO_ID + "`**\n*" + Products.AMMO_INFO + "*", false);
                embed.addField("Shield", "Cost: **`" + myFormat.format(Products.SHIELD) + "`**\nID: **`" + Products.SHIELD_ID + "`**\n*" + Products.SHIELD_INFO + "*", false);
                embed.addField("Ecstasy", "Cost: **`" + myFormat.format(Products.ECSTASY) + "`**\nID: **`" + Products.ECSTACY_ID + "`**\n*" + Products.ECSTACY_INFO + "*", false);
                embed.setFooter(String.format("Page %s/2", i + 1), Objects.requireNonNull(event.getMember()).getUser().getEffectiveAvatarUrl());
                embed.setTimestamp(Instant.now());
                embed.setDescription("Current Sale: **~~" + Products.SALE + "%~~**");
                embed.setColor(Utility.getAmbientColor());
            }
            if (i == 1) {
                embed.clear();
                NumberFormat myFormat = NumberFormat.getInstance();
                embed.setTitle(String.format("%s Marketplace", event.getGuild().getName()));
                embed.addField("Nitro Code", "**1.** Cost: **`" + myFormat.format(Products.NITRO_CODE) + "`**\n **2.** Stock: **`" + Products.NITRO_CODE_STOCK + "`**\n **3.** ID: **`" + Products.NITRO_CODE_ID + "`**", true);
                embed.addField("Custom Role", "**4.** Cost: **`" + myFormat.format(Products.CUSTOM_ROLE) + "`**\n **5.** Stock: **`\u221E`**\n **6.** ID: **`" + Products.CUSTOM_ROLE_ID + "`**", true);
                embed.addField("Hoisted Role", "**7.** Cost: **`" + myFormat.format(Products.HOISTED_ROLE) + "`**\n **8.** Stock: **`\u221E`**\n **9.** ID: **`" + Products.HOISTED_ROLE_ID + "`**", true);
                embed.addField("Custom VC", "**10.** Cost: **`" + myFormat.format(Products.CUSTOM_VC) + "`**\n **11.** Stock: **`\u221E`**\n **12.** ID: **`" + Products.CUSTOM_VC_ID + "`**", true);
                embed.addField("VC Perms", "**13.** Cost: **`" + myFormat.format(Products.VC_PERMS) + "`**\n **14.** Stock: **`\u221E`**\n **15.** ID: **`" + Products.VC_PERMS_ID + "`**", true);
                embed.addField("Staff Role", "**16.** Cost: **`" + myFormat.format(Products.STAFF_ROLE) + "`**\n **17** Stock: **`\u221E`**\n **18.** ID: **`" + Products.STAFF_ROLE_ID + "`**", true);
                embed.setFooter(String.format("Page %s/2", i + 1), Objects.requireNonNull(event.getMember()).getUser().getEffectiveAvatarUrl());
                embed.setTimestamp(Instant.now());
                embed.setDescription("Current Sale: **~~" + Products.SALE + "%~~**");
                embed.setColor(Utility.getAmbientColor());
            }
            pages.add(new Page(PageType.EMBED, embed.build()));
        }
        event.getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
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
