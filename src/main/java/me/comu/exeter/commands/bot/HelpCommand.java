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

        if (args.get(0).equalsIgnoreCase("admin")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.ADMIN)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Admin Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("bot")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.BOT)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Bot Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("economy")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.ECONOMY)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Economy Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("marriage")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.MARRIAGE)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Marriage Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("misc")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.MISC)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Misc Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("moderation")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.MODERATION)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Moderation Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("music")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.MUSIC)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Music Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("owner")) {
            if (event.getAuthor().getIdLong() != Core.OWNERID)
            {
                event.getChannel().sendMessage("You aren't authorized to execute that command").queue();
                return;
            }
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands()) {
                if (command.getCategory().equals(Category.OWNER)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`\n");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Owner Commands (" + count + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
            embedBuilder.setDescription(buffer.toString());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }


        String joined = String.join("", args);

        ICommand command = manager.getCommand(joined);

        if (command == null) {
            event.getChannel().sendMessage("Command `" + joined + "` does not exist\n" +
                    "Use `" + Core.PREFIX + getInvoke() + "` for a list of commands").queue();
            return;
        }

        String message = "__**" + command.getInvoke() + "** Command__:\n" + command.getHelp() + "";

        event.getChannel().sendMessage(message).queue();

    }

    private void embedHandler(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Categories (" + Category.values().length + ')').setColor(0xFF633B).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        StringBuilder stringBuilder = embedBuilder.getDescriptionBuilder();
        Arrays.stream(Category.values()).forEach((category) -> stringBuilder.append('`').append(category.name()).append("`\n"));
        stringBuilder.append("\n").append(Core.PREFIX).append("help [**category**]\n").append(Core.PREFIX).append("help [**command**]\n\n*Made with \u2764 by swag*");
        event.getChannel().sendMessage(embedBuilder.build()).queue();


    }
/*    private void embedHandler(GuildMessageReceivedEvent event) {
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

    }*/

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
        return new String[]{"assistance", "halp", "autism", "cmds", "commands"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
