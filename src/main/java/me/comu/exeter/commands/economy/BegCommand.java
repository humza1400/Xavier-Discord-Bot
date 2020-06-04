package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BegCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (EconomyManager.verifyUser(Objects.requireNonNull(event.getMember()).getUser().getId())) {
            EconomyManager.getUsers().put(event.getMember().getUser().getId(), 0);
        }
        double d = Math.random();
        if (d > 0.29) {
            event.getChannel().sendMessage("Keep begging bud...").queue();
        } else {
            int begMoney = Wrapper.randomNum(0, 10);
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + begMoney);
            event.getChannel().sendMessage(String.format("Aight, **%s**, I'll pity you with **%s** credits.", event.getMember().getEffectiveName(), begMoney).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")).queue();
        }

        EcoJSONHandler.saveEconomyConfig();
    }

    @Override
    public String getHelp() {
        return "Begs for credits on the skreets\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "beg";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
