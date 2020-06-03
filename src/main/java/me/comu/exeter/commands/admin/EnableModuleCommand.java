package me.comu.exeter.commands.admin;

import me.comu.exeter.core.CommandManager;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.*;

public class EnableModuleCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to enable modules").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("You need to specify a module to enable").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase(getInvoke()) || Arrays.asList(getAlias()).contains(args.get(0))) {
            event.getChannel().sendMessage("The enable module is always enabled").queue();
            return;
        }
        if (DisableModuleCommand.disabledModules.isEmpty()) {
            event.getChannel().sendMessage("No modules are current disabled").queue();
            return;
        }
        if (isInListOfHash(DisableModuleCommand.disabledModules, args.get(0))) {
            try {
                Class commandClass = Class.forName(DisableModuleCommand.disabledModules.get(returnListFromHash(DisableModuleCommand.disabledModules, args.get(0))).getClass().getName());
                CommandManager.register((ICommand) commandClass.newInstance());
                event.getChannel().sendMessage("Successfully enabled " + MarkdownUtil.bold(DisableModuleCommand.disabledModules.get(returnListFromHash(DisableModuleCommand.disabledModules, args.get(0))).getInvoke()) + " in " + MarkdownUtil.monospace(DisableModuleCommand.disabledModules.get(returnListFromHash(DisableModuleCommand.disabledModules, args.get(0))).getCategory().toString()) + ".").queue();
                DisableModuleCommand.disabledModules.remove(returnListFromHash(DisableModuleCommand.disabledModules, args.get(0)));
            } catch (ClassNotFoundException ex) {
                event.getChannel().sendMessage(MarkdownUtil.monospace(args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")) + " is not disabled or it doesn't exist.").queue();
            } catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessage("Something went wrong! :(").queue();
            }
        } else {
            try {
                Class commandClass = Class.forName("me.comu.exeter.commands.moderation.MuteCommand");
                CommandManager.register((ICommand) commandClass.newInstance());
                event.getChannel().sendMessage(MarkdownUtil.monospace(args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")) + " is not disabled or it doesn't exist.").queue();
            } catch (Exception ex) {
                event.getChannel().sendMessage(ex.getMessage()).queue();
            }
        }
    }

    private static boolean isInListOfHash(Map<List<String>, ICommand> hashMap, String word) {
        for (List<String> list : hashMap.keySet()) {
            for (String string : list)
                if (string.equalsIgnoreCase(word))
                    return true;
        }
        return false;
    }
    private static List<String> returnListFromHash(Map<List<String>, ICommand> hashMap, String word) {
        List<String> fart = new ArrayList<>();
        for (List<String> list : hashMap.keySet()) {
            for (String string : list)
                if (string.equalsIgnoreCase(word))
                {
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
}
