package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ARSaveConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != Core.OWNERID)
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission.").build()).queue();
            return;
        }
        Core.getInstance().saveConfig(Core.getInstance().getWhitelistedHandler());
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully saved config: `whitelisted.json`.").build()).queue();
    }

    @Override
    public String getHelp() {
        return "Force saves the current anti-raid config\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "arsave";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"arsavecfg","arsaveconfig","savewhitelist"};
    }

     @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
