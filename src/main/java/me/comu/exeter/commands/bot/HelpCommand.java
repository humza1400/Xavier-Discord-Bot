package me.comu.exeter.commands.bot;

import me.comu.exeter.commands.CommandManager;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    // paginate the commands lol

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            event.getChannel().sendMessage("I need permission `MESSAGE_EMBED_LINKS`.").queue();
            return;
        }
        if (args.isEmpty()) {
            embedHandler(event);
            return;
        }

        if (args.get(0).equalsIgnoreCase("admin")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.ADMIN)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Admin Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959641571924533268/958561665648042034.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("bot")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.BOT)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Bot Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959648276389433356/947227929090359326.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("economy")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.ECONOMY)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Economy Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959637536916004914/xavier_economy.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("marriage")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.MARRIAGE)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Marriage Commands (" + count + ')', "https://www.comp.life","https://cdn.discordapp.com/attachments/723250694118965300/959644350789337098/725187912978923583.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("nsfw")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.NSFW)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("NSFW Commands (" + count + ')', "https://www.comp.life","https://cdn.discordapp.com/attachments/723250694118965300/960631338950619276/817699582598709279.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("misc")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.MISC)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Misc Commands (" + count + ')', "https://www.comp.life","https://cdn.discordapp.com/attachments/723250694118965300/959652546421608448/954579854450380840.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("moderation")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.MODERATION)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Moderation Commands (" + count + ')', "https://www.comp.life","https://cdn.discordapp.com/attachments/723250694118965300/959648833841811466/958147870106722344.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("music")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.MUSIC)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Music Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959637981000511528/200.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("image")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.IMAGE)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Image Manipulation Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959645342482178098/958178089588908032.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("ticket")) {
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.TICKET)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Ticket Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959648833552416888/947225848510033970.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        } else if (args.get(0).equalsIgnoreCase("owner")) {
            if (event.getAuthor().getIdLong() != Core.OWNERID) {
                event.getChannel().sendMessage("You aren't authorized to execute that command").queue();
                return;
            }
            StringBuilder buffer = new StringBuilder();
            int count = 0;
            for (ICommand command : manager.getCommands().values()) {
                if (command.getCategory().equals(Category.OWNER)) {
                    if (!buffer.toString().contains(command.getInvoke())) {
                        buffer.append("`").append(command.getInvoke()).append("`, ");
                        count++;
                    }
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Owner Commands (" + count + ')', "https://www.comp.life", "https://cdn.discordapp.com/attachments/723250694118965300/959641951219634296/958148145827676200.gif").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setDescription(buffer.substring(0, buffer.length()-2));
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }


        String joined = String.join("", args);

        ICommand command = manager.getCommand(joined);

        if (command == null) {
            event.getChannel().sendMessage("Command `" + joined + "` does not exist\n" +
                    "Use `" + Core.PREFIX + getInvoke() + "` for a list of commands").queue();
            return;
        }

        String picUrl;
        switch (command.getCategory()) {
            case BOT:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959648276389433356/947227929090359326.gif";
                break;
            case MISC:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959652546421608448/954579854450380840.gif";
                break;
            case NSFW:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/960631338950619276/817699582598709279.gif";
                break;
            case ADMIN:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959641571924533268/958561665648042034.gif";
                break;
            case IMAGE:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959645342482178098/958178089588908032.gif";
                break;
            case MUSIC:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959637981000511528/200.gif";
                break;
            case OWNER:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959641951219634296/958148145827676200.gif";
                break;
            case TICKET:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959648833552416888/947225848510033970.gif";
                break;
            case ECONOMY:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959637536916004914/xavier_economy.gif";
                break;
            case MARRIAGE:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959644350789337098/725187912978923583.gif";
                break;
            case MODERATION:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959648833841811466/958147870106722344.gif";
                break;
            default:
                picUrl = "https://cdn.discordapp.com/attachments/723250694118965300/959633689111838730/comp_help.gif";
                break;
        }
        event.getChannel().sendMessageEmbeds(Utility.embed(command.getHelp()).setAuthor(command.getInvoke() + " command", "https://www.comp.life", picUrl).build()).queue();

    }

    private void embedHandler(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Categories (" + Category.values().length + ')').setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
        StringBuilder stringBuilder = embedBuilder.getDescriptionBuilder();
        Arrays.stream(Category.values()).forEach((category) -> stringBuilder.append('`').append(category.name()).append("`\n"));
        stringBuilder.append("\n").append(Core.PREFIX).append("help [**category**]\n").append(Core.PREFIX).append("help [**command**]\n\n*Made with <a:compheart:960476313829920768> by Swag*");
        embedBuilder.setFooter(Category.values().length + " Categories | " + Core.getInstance().getCommandManager().getCommands().values().size() + " Commands");
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
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
        return new String[]{"assistance", "halp", "autism", "cmds", "commands", "?"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
