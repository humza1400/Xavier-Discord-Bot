package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RobCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId())) {
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
        }
        if (memberList.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify someone to rob from.").build()).queue();
            return;
        }
        if (EconomyManager.verifyUser(memberList.get(0).getUser().getId()))
            EconomyManager.getUsers().put(memberList.get(0).getUser().getId(), 0);
        if (EconomyManager.getBalance(event.getMember().getId()) < 250) {
            event.getChannel().sendMessageEmbeds(Utility.embed("You need at least **250** credits to try and rob someone.").build()).queue();
            return;
        }
        if (!InventoryCommand.glock.contains(event.getMember().getId()) && !InventoryCommand.draco.contains(event.getMember().getId())) {
            event.getChannel().sendMessageEmbeds(Utility.embed("You don't have a gun to rob someone with, and by the looks of things you're in no shape to fist-fight someone LMAO.").build()).queue();
            return;
        }
        if (!InventoryCommand.ammo.containsKey(event.getMember().getId()) || InventoryCommand.ammo.get(event.getMember().getId()) < 0) {
            event.getChannel().sendMessageEmbeds(Utility.embed("You have no bullets to rob someone with, quit bein' stupid and go buy some from the store.").build()).queue();
            return;
        }
        if (InventoryCommand.protection.contains(memberList.get(0).getId())) {
            event.getChannel().sendMessageEmbeds(Utility.embed("That user is currently under federal protection, wait until it expires.").build()).queue();
            return;
        }

        Member member = memberList.get(0);
        double d = Math.random();
        double x = Math.random();
        if (x <= 0.3) {
            if (InventoryCommand.glock.contains(event.getMember().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You tried pulling the heater out on **" + Utility.removeMarkdown(member.getUser().getAsTag()) + "** but it jammed and broke, cop another one from the shop").build()).queue();
                InventoryCommand.glock.remove(event.getMember().getId());
                return;
            }
            if (InventoryCommand.draco.contains(event.getMember().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You tried pulling the heater out on **" + Utility.removeMarkdown(member.getUser().getAsTag()) + "** but it jammed and broke, cop another one from the shop").build()).queue();
                InventoryCommand.draco.remove(event.getMember().getId());
                return;
            }
        }
        if (d >= 0.75) {
            if (InventoryCommand.shield.containsKey(member.getId()) && InventoryCommand.shield.get(member.getId()) > 0) {
                event.getChannel().sendMessageEmbeds(Utility.embed(Utility.removeMarkdown(memberList.get(0).getUser().getAsTag()) + " currently has **" + InventoryCommand.shield.get(memberList.get(0).getId()) + "** shield, but you just broke one").build()).queue();
                InventoryCommand.shield.replace(member.getId(), InventoryCommand.shield.get(member.getId()) - 1);
                return;
            }
            if (InventoryCommand.draco.contains(event.getMember().getId()))
            {
                if (d < .45)
                {
                    int robbedBalance = EconomyManager.getBalance(member.getUser().getId());
                    EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + robbedBalance);
                    EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) - robbedBalance);
                    InventoryCommand.ammo.replace(event.getMember().getId(), InventoryCommand.ammo.get(event.getMember().getId()) - 1);
                    event.getChannel().sendMessageEmbeds(Utility.embed(String.format("%s just caught %s lacking and finnesed him for all his cash using a Draco LMAO ($**%s**).", event.getMember().getAsMention(), member.getAsMention(), robbedBalance)).build()).queue();
                    return;
                }
            }
            int robbedBalance = Utility.randomNum(0, (EconomyManager.getBalance(member.getUser().getId()) / 8));
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + robbedBalance);
            EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) - robbedBalance);
            InventoryCommand.ammo.replace(event.getMember().getId(), InventoryCommand.ammo.get(event.getMember().getId()) - 1);
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("%s just caught %s lacking and finnesed him for **%s** credits LMAO.", event.getMember().getAsMention(), member.getAsMention(), robbedBalance)).build()).queue();
        } else if (d <= 0.45) {
            EconomyManager.setBalance(event.getMember().getId(), EconomyManager.getBalance(event.getMember().getId()) - EconomyManager.getBalance(event.getMember().getId()) / 8);
            InventoryCommand.ammo.replace(event.getMember().getId(), InventoryCommand.ammo.get(event.getMember().getId()) - 1);
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("%s failed to rob %s and lost **%s** credits, yikes.", event.getMember().getAsMention(), member.getAsMention(), EconomyManager.getBalance(event.getMember().getId()) / 8)).build()).queue();
        } else {
            int robbedBalance = Utility.randomNum(0, (EconomyManager.getBalance(event.getMember().getUser().getId()) / 8));
            EconomyManager.setBalance(member.getUser().getId(), EconomyManager.getBalance(member.getUser().getId()) + robbedBalance);
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) - robbedBalance);
            InventoryCommand.ammo.replace(event.getMember().getId(), InventoryCommand.ammo.get(event.getMember().getId()) - 1);
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("%s stay with the strap and instead robbed %s for **%s** credits LOL.", member.getAsMention(), event.getMember().getAsMention(), robbedBalance)).build()).queue();
        }

        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());

    }

    @Override
    public String getHelp() {
        return "Robs another user, need to have purchased a weapon\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rob";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"finnese", "finesse"};
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
