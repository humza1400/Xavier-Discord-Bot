package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WouldYouRatherCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            Document document = Jsoup.connect("https://www.conversationstarters.com/wyrqlist.php").timeout(6000).get();
            String firstOption = document.select("div#qa").first().text();
            String secondOption = document.select("div#qb").first().text();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Would You Rather:");
            embedBuilder.addField("", firstOption, false);
            embedBuilder.addField("", "or", false);
            embedBuilder.addField("", secondOption, false);
//            embedBuilder.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = (random.nextInt(2000) + 1000) / 10000f;
            final float luminance = 0.9f;
            final Color color = Color.getHSBColor(hue, saturation, luminance);
            embedBuilder.setColor(color);
            event.getChannel().sendMessage(embedBuilder.build()).queue(message -> {
                message.addReaction("\u0031\u20E3").queue();
                message.addReaction("\u0032\u20E3").queue();
            });
        } catch (IOException ex)
        {
            event.getChannel().sendMessage("Caught IOException").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Returns a conversation topic\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "wouldyourather";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"wyr"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
