package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExportCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        List<File> files = getFiles();
        if (files == null) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong and I couldn't load any files.").build()).queue();
            return;
        }
        if (args.isEmpty() || args.get(0).equalsIgnoreCase("list")) {
            StringBuilder stringBuilder = new StringBuilder();
            files.forEach(file -> stringBuilder.append("`").append(file.getName()).append("`\n"));
            event.getChannel().sendMessageEmbeds(Utility.embed(stringBuilder.toString()).setTitle("Available Files (" + files.size() + ")").build()).queue();
        } else if (args.get(0).equalsIgnoreCase("all")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Exporting `" + files.size() + "` files.").build()).queue();
            for (File file : files) {
                event.getChannel().sendFile(file).queue();
            }
        } else {
            boolean found = false;
            for (File file : files) {
                if (file.getName().equalsIgnoreCase(cleanFileName(args.get(0)))) {
                    found = true;
                    event.getChannel().sendFile(file).queue();
                }
            }
            if (!found) {
                event.getChannel().sendMessageEmbeds(Utility.embed("No file found with name `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "`").build()).queue();
            }
        }
    }

    private String cleanFileName(String file) {
        return file.toLowerCase(Locale.ROOT).endsWith(".json") ? file : file + ".json";
    }

    private List<File> getFiles() {
        try (Stream<Path> walk = Files.walk(Paths.get(""))) {
            return walk.map(Path::toFile).filter(f -> f.getName().endsWith(".json")).collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String getHelp() {
        return "Sends the specified JSON file as an attachment\n`" + Core.PREFIX + getInvoke() + " <file>/<all>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "export";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"sendjson", "sendfile"};
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
