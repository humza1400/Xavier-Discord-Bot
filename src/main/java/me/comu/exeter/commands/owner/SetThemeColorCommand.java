package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SetThemeColorCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have access to change the theme color").build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("theme")) {
            try {
                String input = args.get(1).replace("#", "").replace("0x", "");
                Color color = Color.decode("#" + input);
                Core.getInstance().setColorTheme(color.getRGB());
                event.getChannel().sendMessageEmbeds(Utility.embed("Set the COLOR_THEME to: " + Core.getInstance().getColorTheme()).setColor(Core.getInstance().getColorTheme()).build()).queue();
            } catch (NumberFormatException numberFormatException) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid color").build()).queue();
            }
            return;
        }

        if (args.get(0).equalsIgnoreCase("error")) {
            try {
                String input = args.get(1).replace("#", "").replace("0x", "");
                Color color = Color.decode("#" + input);
                Core.getInstance().setErrorColorTheme(color.getRGB());
                event.getChannel().sendMessageEmbeds(Utility.embed("Set the COLOR_ERROR_THEME to: " + Core.getInstance().getErrorColorTheme()).setColor(Core.getInstance().getErrorColorTheme()).build()).queue();
            } catch (NumberFormatException numberFormatException) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid color").build()).queue();
            }
            return;
        }
        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify whether you'd like to change the `theme` or `error` colors").build()).queue();

    }

    @Override
    public String getHelp() {
        return "Sets the theme\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "settheme";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"theme"};
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
