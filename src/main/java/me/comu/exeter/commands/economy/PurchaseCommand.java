package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.Products;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PurchaseCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a product-id of what you want to purchase `" + Core.PREFIX + "shop`").build()).queue();
            return;
        }

        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId()))
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);

        if (args.get(0).equalsIgnoreCase(Products.PROTECTION_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.PROTECTION) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Protection costs `" + Products.PROTECTION + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (InventoryCommand.protection.contains(event.getMember().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You already have protection, wait until it expires!").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.PROTECTION);
            InventoryCommand.protection.add(event.getMember().getId());
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased 3 hour __Protection__ for **" + Products.PROTECTION + "** credits! :partying_face: :tada:").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.GLOCK_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.GLOCK) {
                event.getChannel().sendMessageEmbeds(Utility.embed("A glock costs `" + Products.GLOCK + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (InventoryCommand.glock.contains(event.getMember().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You already have a glock, pull up on the block already").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.GLOCK);
            InventoryCommand.glock.add(event.getMember().getId());
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Glock__ for **" + Products.PROTECTION + "** credits! :partying_face: :tada:").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.DRACO_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.DRACO) {
                event.getChannel().sendMessageEmbeds(Utility.embed("A draco costs `" + Products.DRACO + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (InventoryCommand.draco.contains(event.getMember().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You already have a draco, spray down your ops already.").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.DRACO);
            InventoryCommand.draco.add(event.getMember().getId());
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Draco__ for **" + Products.DRACO + "** credits! :partying_face: :tada:").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.AMMO_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.AMMO) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Ammo costs `" + Products.AMMO + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (!InventoryCommand.glock.contains(event.getMember().getId()) && !InventoryCommand.draco.contains(event.getMember().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You don't have a gun to buy bullets for, cop one in the shop").build()).queue();
                return;
            }
            if (InventoryCommand.ammo.containsKey(event.getMember().getId())) {
                if (InventoryCommand.draco.contains(event.getMember().getId()) && InventoryCommand.ammo.get(event.getMember().getId()) == 16) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("Your draco can only hold **16** bullets, walk em down").build()).queue();
                    return;
                }
                if (InventoryCommand.glock.contains(event.getMember().getId()) && !InventoryCommand.draco.contains(event.getMember().getId()) && InventoryCommand.ammo.get(event.getMember().getId()) == 8) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("Your glock can only hold **8** bullets, might wanna cop an extended clip on that").build()).queue();
                    return;
                }
                InventoryCommand.ammo.put(event.getMember().getId(), InventoryCommand.ammo.get(event.getMember().getId()) + 1);
            } else {
                InventoryCommand.ammo.put(event.getMember().getId(), 1);
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.AMMO);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased 1x __Ammo__ for **" + Products.AMMO + "** credits! :partying_face: :tada:").build()).queue();

        } else if (args.get(0).equalsIgnoreCase(Products.SHIELD_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.SHIELD) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Shield costs `" + Products.SHIELD + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (InventoryCommand.shield.containsKey(event.getMember().getId())) {
                if (InventoryCommand.shield.get(event.getMember().getId()) == 5) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("You can only hold three shield, cop a armor satchel to hold up to 8!").build()).queue();
                    return;
                } else {
                    InventoryCommand.shield.replace(event.getMember().getId(), InventoryCommand.shield.get(event.getMember().getId()) + 1);
                }
            } else {
                InventoryCommand.shield.put(event.getMember().getId(), 1);
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.SHIELD);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased 1x __Shield__ for **" + Products.SHIELD + "** credits! :partying_face: :tada:").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.ECSTACY_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.ECSTASY) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Ecstacy costs `" + Products.ECSTASY + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (InventoryCommand.ecstasy.containsKey(event.getMember().getId())) {
                if (InventoryCommand.ecstasy.get(event.getMember().getId()) == 24) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can only hold **24** ecstasy pills, buy a drug farm to expand your empire").build()).queue();
                    return;
                } else {
                    InventoryCommand.ecstasy.replace(event.getMember().getId(), InventoryCommand.ecstasy.get(event.getMember().getId()) + 1);
                }
            } else {
                InventoryCommand.ecstasy.put(event.getMember().getId(), 1);
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.ECSTASY);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased 1x __Ecstacy__ for **" + Products.ECSTASY + "** credits! :partying_face: :tada:").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.NITRO_CODE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.NITRO_CODE) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Nitro Codes cost `" + Products.NITRO_CODE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            if (Products.NITRO_CODE_STOCK == 0) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Sorry, but we are currently out of stock for Nitro Codes, please check again later!").build()).queue();
                return;
            }
            Products.NITRO_CODE_STOCK -= 1;
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.NITRO_CODE);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Nitro Code__ for **" + Products.NITRO_CODE + "** credits! :partying_face: :tada: (Check your DMs)").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.CUSTOM_ROLE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.CUSTOM_ROLE) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Custom Roles cost `" + Products.CUSTOM_ROLE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.CUSTOM_ROLE);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Custom Role__ for **" + Products.CUSTOM_ROLE + "** credits! :partying_face: :tada: (Please DM an Admin)").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.HOISTED_ROLE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.HOISTED_ROLE) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Hoisted Roles cost `" + Products.HOISTED_ROLE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.HOISTED_ROLE);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Hoisted Role__ for **" + Products.HOISTED_ROLE + "** credits! :partying_face: :tada: (Please DM an Admin)").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.CUSTOM_VC_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.CUSTOM_VC) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Customs Voice-Channels cost `" + Products.CUSTOM_VC + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.CUSTOM_VC);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Custom Voice-Channel__ for **" + Products.CUSTOM_VC + "** credits! :partying_face: :tada: (Please DM an Admin)").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.VC_PERMS_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.VC_PERMS) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Voice-Channel Permissions cost `" + Products.VC_PERMS + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.VC_PERMS);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased __Voice-Channel Permissions__ for **" + Products.VC_PERMS + "** credits! :partying_face: :tada: (Please DM an Admin)").build()).queue();
        } else if (args.get(0).equalsIgnoreCase(Products.STAFF_ROLE_ID)) {
            if (EconomyManager.getBalance(event.getMember().getId()) < Products.STAFF_ROLE) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Staff Roles cost `" + Products.STAFF_ROLE + "` credits, you only have **" + EconomyManager.getBalance(event.getMember().getId()) + "** credits.").build()).queue();
                return;
            }
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - Products.STAFF_ROLE);
            Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
            event.getChannel().sendMessageEmbeds(Utility.embed(event.getMember().getAsMention() + " has successfully purchased a __Staff Role__ for **" + Products.STAFF_ROLE + "** credits! :partying_face: :tada: (Please DM an Admin)").build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid product-id, please refer to `" + Core.PREFIX + "shop`").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
