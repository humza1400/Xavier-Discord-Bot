package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnableModuleCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to enable modules.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to specify a module to enable.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase(getInvoke()) || Arrays.asList(getAlias()).contains(args.get(0))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't enable the enable module lmao.").build()).queue();
            return;
        }
        if (DisableModuleCommand.disabledModules.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("No modules are currently disabled.").build()).queue();
            return;
        }
        if (isInListOfHash(args.get(0))) {
            try {
                Class<?> commandClass = Class.forName(DisableModuleCommand.disabledModules.get(returnListFromHash(args.get(0))).getClass().getName());
                Core.getInstance().getCommandManager().register((ICommand) commandClass.getDeclaredConstructor().newInstance());
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully enabled " + MarkdownUtil.bold(DisableModuleCommand.disabledModules.get(returnListFromHash(args.get(0))).getInvoke()) + " in " + MarkdownUtil.monospace(DisableModuleCommand.disabledModules.get(returnListFromHash(args.get(0))).getCategory().toString()) + ".").build()).queue();
                DisableModuleCommand.disabledModules.remove(returnListFromHash(args.get(0)));
            } catch (ClassNotFoundException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(MarkdownUtil.monospace(Utility.removeMentions(args.get(0))) + " is not disabled or it doesn't exist.").build()).queue();
            } catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong! :(").build()).queue();
            }
        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find disabled module of `" + Utility.removeMentions(args.get(0)) + "`, maybe it's not disabled.").build()).queue();
        }
    }

    private static boolean isInListOfHash(String word) {
        for (List<String> list : DisableModuleCommand.disabledModules.keySet()) {
            for (String string : list)
                if (string.equalsIgnoreCase(word))
                    return true;
        }
        return false;
    }

    private static List<String> returnListFromHash(String word) {
        List<String> fart = new ArrayList<>();
        for (List<String> list : DisableModuleCommand.disabledModules.keySet()) {
            for (String string : list)
                if (string.equalsIgnoreCase(word)) {
                    fart.addAll(list);
                }
        }
        return fart;
    }


    @Override
    public String getHelp() {
        return "Enables a disabled module on the bot.\n`" + Core.PREFIX + getInvoke() + " [module]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "enablemodule";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"enable", "reenable", "enablemod", "reenablemod"};
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
