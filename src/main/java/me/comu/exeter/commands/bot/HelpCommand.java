package me.comu.exeter.commands.bot;

import me.comu.exeter.core.CommandManager;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            embedHandler(event);
            return;
        }

        String joined = String.join("", args);

        ICommand command = manager.getCommand(joined);

        if(command == null) {
            event.getChannel().sendMessage("Command `" + joined + "` does not exist\n" +
                    "Use `" + Core.PREFIX + getInvoke() + "` for a list of commands").queue();
            return;
        }

        String message = "Command `" + command.getInvoke() + "`\n" + command.getHelp() + "";

        event.getChannel().sendMessage(message).queue();

    }

    private void embedHandler(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Commands (" + manager.getCommands().size() + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        StringBuilder stringBuilder = embedBuilder.getDescriptionBuilder();
            manager.getCommands().forEach((command) -> stringBuilder.append('`').append(command.getInvoke()).append("` ")
            );
        try {
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (IllegalStateException ex)
        {
            event.getChannel().sendMessage("null").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Shows all bot features\n" + "`" + Core.PREFIX + getInvoke() + " [command]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "help";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"assistance","halp","autism","cmds","commands"};
    }
}
