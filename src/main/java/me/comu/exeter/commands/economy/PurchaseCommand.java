package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.Products;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PurchaseCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a product-id of what you want to purchase `" + Core.PREFIX + "shop`").queue();
            return;
        }

        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId()))
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);

        if (args.get(0).equalsIgnoreCase(Products.NITRO_CODE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.NITRO_CODE) {
                event.getChannel().sendMessage("Nitro Codes cost `" + Products.NITRO_CODE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").queue();
                return;
            }
            if (Products.NITRO_CODE_STOCK == 0)
            {
                event.getChannel().sendMessage("Sorry, but we are currently out of stock for Nitro Codes, please check again later!").queue();
                return;
            }
            Products.NITRO_CODE_STOCK -= 1;
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.NITRO_CODE);
            EcoJSONHandler.saveEconomyConfig();
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has successfully purchased a __Nitro Code (" + Products.NITRO_CODE_ID + ")__ for **" + Products.NITRO_CODE + "** credits! :partying_face: :tada: (Check your DMs)").queue();
        } else if (args.get(0).equalsIgnoreCase(Products.CUSTOM_ROLE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.CUSTOM_ROLE) {
                event.getChannel().sendMessage("Custom Roles cost `" + Products.CUSTOM_ROLE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId() + "** credits.")).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.CUSTOM_ROLE);
            EcoJSONHandler.saveEconomyConfig();
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has successfully purchased a __Custom Role (" + Products.CUSTOM_ROLE_ID + ")__ for **" + Products.CUSTOM_ROLE + "** credits! :partying_face: :tada: (Please DM an Admin)").queue();
        } else if (args.get(0).equalsIgnoreCase(Products.HOISTED_ROLE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.HOISTED_ROLE) {
                event.getChannel().sendMessage("Hoisted Roles cost `" + Products.HOISTED_ROLE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId() + "** credits.")).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.HOISTED_ROLE);
            EcoJSONHandler.saveEconomyConfig();
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has successfully purchased a __Hoisted Role (" + Products.HOISTED_ROLE_ID + ")__ for **" + Products.HOISTED_ROLE + "** credits! :partying_face: :tada: (Please DM an Admin)").queue();
        } else if (args.get(0).equalsIgnoreCase(Products.CUSTOM_VC_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.CUSTOM_VC) {
                event.getChannel().sendMessage("Customs Voice-Channels cost `" + Products.CUSTOM_VC + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId() + "** credits.")).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.CUSTOM_VC);
            EcoJSONHandler.saveEconomyConfig();
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has successfully purchased a __Custom Voice-Channel (" + Products.CUSTOM_VC_ID + ")__ for **" + Products.CUSTOM_VC + "** credits! :partying_face: :tada: (Please DM an Admin)").queue();
        } else if (args.get(0).equalsIgnoreCase(Products.VC_PERMS_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.VC_PERMS) {
                event.getChannel().sendMessage("Voice-Channel Permissions cost `" + Products.VC_PERMS + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId() + "** credits.")).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.VC_PERMS);
            EcoJSONHandler.saveEconomyConfig();
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has successfully purchased __Voice-Channel Permissions (" + Products.VC_PERMS_ID + ")__ for **" + Products.VC_PERMS + "** credits! :partying_face: :tada: (Please DM an Admin)").queue();
        } else if (args.get(0).equalsIgnoreCase(Products.STAFF_ROLE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.STAFF_ROLE) {
                event.getChannel().sendMessage("Staff Roles cost `" + Products.STAFF_ROLE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId() + "** credits.")).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.STAFF_ROLE);
            EcoJSONHandler.saveEconomyConfig();
            event.getChannel().sendMessage(event.getMember().getAsMention() + " has successfully purchased a __Staff Role (" + Products.STAFF_ROLE_ID + ")__ for **" + Products.STAFF_ROLE + "** credits! :partying_face: :tada: (Please DM an Admin)").queue();
        }
        else {
            event.getChannel().sendMessage("Invalid product-id, please refer to `" + Core.PREFIX + "shop`").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Buys the specified product from the marketplace\n" + "`" + Core.PREFIX + getInvoke() + " [product-id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "purchase";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"buy"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
