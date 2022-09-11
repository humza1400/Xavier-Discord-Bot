package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DisableModuleCommand implements ICommand {

    public static final Map<List<String>, ICommand> disabledModules = new ConcurrentHashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to disable modules.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You need to specify a module to disable.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase(getInvoke()) || Arrays.asList(getAlias()).contains(args.get(0))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't disable the disable module lmao.").build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("disable") || args.get(0).equalsIgnoreCase("disablemodule") || args.get(0).equalsIgnoreCase("disablemod")) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't disable the enable module lmao.").build()).queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("help") || args.get(0).equalsIgnoreCase("assistance") || args.get(0).equalsIgnoreCase("halp") || args.get(0).equalsIgnoreCase("autism") || args.get(0).equalsIgnoreCase("cmds") || args.get(0).equalsIgnoreCase("commands") || args.get(0).equalsIgnoreCase("?")) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't disable the help module.").build()).queue();
            return;
        }
        for (ICommand command : Core.getInstance().getCommandManager().getCommands().values()) {
            if (args.get(0).equalsIgnoreCase(command.getInvoke()) || Arrays.asList(command.getAlias()).contains(args.get(0).toLowerCase())) {
                {
                    if (disabledModules.containsValue(command)) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("**" + command.getInvoke() + "** is already disabled.").build()).queue();
                        return;
                    }
                    List<String> list = new ArrayList<>();
                    list.add(command.getInvoke());
                    list.addAll(Arrays.asList(command.getAlias()));
                    disabledModules.put(list, command);
                    Core.getInstance().getCommandManager().unregister(command);
                    event.getChannel().sendMessageEmbeds(Utility.embed("Successfully disabled " + MarkdownUtil.bold(command.getInvoke()) + " in " + MarkdownUtil.monospace(command.getCategory().toString()) + ".").build()).queue();
                    return;
                }
            }
        }
        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find module " + MarkdownUtil.monospace(Utility.removeMentions(args.get(0))) + ". Maybe it's already disabled.").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
