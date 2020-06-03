package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class CoronavirusCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            Document document = Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
            String cases = document.getAllElements().get(0).text().replaceAll("Coronavirus Update \\(Live\\): ","").substring(0,document.getAllElements().get(0).text().replaceAll("Coronavirus Update \\(Live\\): ","").indexOf(" "));
            String deaths = document.getAllElements().get(0).text().substring(40).replaceFirst(" ", "").replaceFirst(" ", "").substring(0, document.getAllElements().get(0).text().substring(40).replaceFirst(" ", "").replaceFirst(" ", "").indexOf(" ")).replaceAll("[A-Za-z]","");
            String recovered = document.getAllElements().get(103).text();
            String activeCases = document.getAllElements().get(108).text().replace("Active Cases","").trim().substring(0, document.getAllElements().get(108).text().replace("Active Cases","").trim().indexOf(" "));
            String closedCases = document.getAllElements().get(140).text();
            String infected = document.getAllElements().get(119).text();
            String preTime = document.getAllElements().get(79).text().replace("GMT","").substring(document.getAllElements().get(79).text().replace("GMT","").length()-6).trim();
            String postTime = LocalTime.parse(preTime, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
            String preDate = document.getAllElements().get(79).text().replace("GMT","").replace(preTime, "").trim();
            String date = preDate + " "+ postTime;
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("COVID-19 Pandemic").setColor(Color.RED).setFooter(date, "https://bit.ly/3gTb2Ug")
                    .addField("Total Cases:", cases, true)
                    .addField("Active Cases:", activeCases, true)
                    .addField("Closed Cases:", closedCases, true)
                    .addField("Infected:", infected, true)
                    .addField("Deaths:", deaths, true)
                    .addField("Recovered:", recovered, true)
                    .build()).queue();
        } catch (IOException ex) {
            event.getChannel().sendMessage("Error'd when trying to scrape data").queue();
        }
    }

    @Override
    public String getHelp() {
        return "See the current Coronavirus statistics\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "coronavirus";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"corona", "coronastats", "cvirus"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
