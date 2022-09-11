package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class EcoSaveConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Core.getInstance().saveConfig(Core.getInstance().getEcoHandler());
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully saved config: **economy.json**.").build()).queue();

    }

    @Override
    public String getHelp() {
        return "Force saves the current economy config\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ecosave";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"saveecoconfig","ecosaveconfig"};
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
