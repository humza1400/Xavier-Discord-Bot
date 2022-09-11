package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
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
            event.getChannel().sendMessageEmbeds(Utility.embed("Keep begging bud...").build()).queue();
        } else {
            int begMoney = Utility.randomNum(0, 10);
            EconomyManager.setBalance(event.getMember().getUser().getId(), EconomyManager.getBalance(event.getMember().getUser().getId()) + begMoney);
            event.getChannel().sendMessageEmbeds(Utility.embed(Utility.removeMentions(String.format("Aight, **%s**, I'll pity you with **%s** credits.", event.getMember().getEffectiveName(), begMoney))).build()).queue();
        }

        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
