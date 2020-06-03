package me.comu.exeter.commands.admin;

import me.comu.exeter.core.CommandManager;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DisableModuleCommand implements ICommand {

    public static Map<List<String>, ICommand> disabledModules = new ConcurrentHashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to disable modules").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("You need to specify a module to disable").queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase(getInvoke()) || Arrays.asList(getAlias()).contains(args.get(0))) {
            event.getChannel().sendMessage("You can't disable the disable module lmao").queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("help") || args.get(0).equalsIgnoreCase("assistance") || args.get(0).equalsIgnoreCase("halp") || args.get(0).equalsIgnoreCase("autism") || args.get(0).equalsIgnoreCase("cmds") || args.get(0).equalsIgnoreCase("commands") || args.get(0).equalsIgnoreCase("?")) {
            event.getChannel().sendMessage("You can't disable the help module").queue();
            return;
        }
        for (ICommand command : CommandManager.commands.values()) {
            if (args.get(0).equalsIgnoreCase(command.getInvoke()) || Arrays.asList(command.getAlias()).contains(args.get(0).toLowerCase())) {
                {
                    if (disabledModules.containsValue(command)) {
                        event.getChannel().sendMessage("That command is already disabled").queue();
                        return;
                    }
                    List<String> list = new ArrayList<>();
                    list.add(command.getInvoke());
                    list.addAll(Arrays.asList(command.getAlias()));
                    disabledModules.put(list, command);
                    CommandManager.unregister(command);
                    event.getChannel().sendMessage("Successfully disabled " + MarkdownUtil.bold(command.getInvoke()) + " in " + MarkdownUtil.monospace(command.getCategory().toString()) + ".").queue();
                    return;
                }
            }
        }
        event.getChannel().sendMessage("Couldn't find module " + MarkdownUtil.monospace(args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere")) + ". Maybe it's already disabled.").queue();


    }

    @Override
    public String getHelp() {
        return "Disables a module on the bot so it won't be functional until re-enabled.\n`" + Core.PREFIX + getInvoke() + " [module]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "disablemodule";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"disable", "disablemod"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
