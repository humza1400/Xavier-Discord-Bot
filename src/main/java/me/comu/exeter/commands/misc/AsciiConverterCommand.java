package me.comu.exeter.commands.misc;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsciiConverterCommand implements ICommand {

    private final static String asciiArtUrl = "http://artii.herokuapp.com/";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            input.append(i == args.size() - 1 ? args.get(i) : args.get(i) + " ");
        }

        List<String> fonts = getAsciiFonts();
        String font = fonts.get(Wrapper.randomNum(0, fonts.size() - 1));

        try {
            String ascii = getAsciiArt(input.toString(), font);

            if (ascii.length() > 1900) {
                event.getChannel().sendMessage("```fix\n\nThe ascii text is too large```").queue();
                return;
            }

            event.getChannel().sendMessage("```fix\n\n" + ascii + "```").queue();
        } catch (IllegalArgumentException iae) {
            event.getChannel().sendMessage("```Text contains invalid characters```").queue();
        }
    }

     private static String getAsciiArt(String ascii, String font) {
        try {
            String url = asciiArtUrl + "make" + "?text=" + ascii.replaceAll(" ", "+") +
                    (font == null || font.isEmpty() ? "" : "&font=" + font);
            return Unirest.get(url).asString().getBody();
        } catch (UnirestException e) {
            return "Fail to get the ascii art.";
        }
    }

    private static List<String> getAsciiFonts() {
     /*   String url = asciiArtUrl + "fonts_list";
        List<String> fontList = new ArrayList<>();
        try {
            String list = Unirest.get(url).asString().getBody();

            fontList = Arrays.stream(list.split("\n")).collect(Collectors.toList());

        } catch (UnirestException e) {
            e.printStackTrace();
        }*/
        List<String> fontList = new ArrayList<>();
        fontList.add("colossal");
        fontList.add("acrobatic");
        fontList.add("crawford");
        fontList.add("clr6x8");
        return fontList;
    }

    @Override
    public String getHelp() {
        return "Converts string-types to ascii fonts\n`" + Core.PREFIX + getInvoke() + " [string]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ascii";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
