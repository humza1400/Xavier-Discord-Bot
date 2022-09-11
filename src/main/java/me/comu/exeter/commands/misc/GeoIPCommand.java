package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GeoIPCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid ip address.").build()).queue();
            return;
        }
        String ip = args.get(0);
        if (ip.matches("[a-zA-Z]+") || !(ip.contains(".")) || ip.contains(",") || ip.contains("!") || ip.contains("@") || ip.contains("-") || ip.contains("_") || ip.contains("+") || ip.contains("=") || ip.contains("'") || ip.contains("\"\"") || ip.contains(":") || ip.contains(";") || ip.contains("\\") || ip.contains("|") || ip.contains("[") || ip.contains("{") || ip.contains("]") || ip.contains("}") || ip.contains("#") || ip.contains("$") || ip.contains("%") || ip.contains("^") || ip.contains("&") || ip.contains("*") || ip.contains("(") || ip.contains(")") || ip.contains("<") || ip.contains(">") || ip.contains("?") || ip.contains("`") || ip.contains("~")) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid IP.").build()).queue();
            return;
        }


        try {
            final URL url = new URL("https://geoiptool.com/en/?ip=" + (ip));
            Document document = Jsoup.connect(url.toString()).timeout(6000).get();
            String hostname = document.select("div.data-item").get(0).text().replaceAll("Hostname:", "");
            String ipaddress = document.select("div.data-item").get(1).text().replaceAll("IP Address:", "");
            String country = document.select("div.data-item").get(2).text().replaceAll("Country:", "");
            String countrycode = document.select("div.data-item").get(3).text().replaceAll("Country Code:", "");
            String region = document.select("div.data-item").get(4).text().replaceAll("Region:", "");
            String city = document.select("div.data-item").get(5).text().replaceAll("City:", "");
            String postalcode = document.select("div.data-item").get(6).text().replaceAll("Postal Code:", "");
            String latitude = document.select("div.data-item").get(7).text().replaceAll("Latitude:", "");
            String longitude = document.select("div.data-item").get(8).text().replaceAll("Longitude:", "");
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Core.getInstance().getColorTheme());
            embed.setTitle("Geo Location Look-Up");
            embed.addField("Hostname", "**" + hostname + "**", true);
            embed.addField("IP Address", "**" + ipaddress + "**", true);
            embed.addField("Country", "**" + country + "**", true);
            embed.addField("Country Code", "**" + countrycode + "**", true);
            embed.addField("Region", "**" + region + "**", true);
            embed.addField("City", "**" + city + "**", true);
            embed.addField("Postal Code", "**" + postalcode + "**", true);
            embed.addField("Latitude", "**" + latitude + "**", true);
            embed.addField("Longitude", "**" + longitude + "**", true);
            embed.setFooter("Requested by " + Objects.requireNonNull(event.getMember()).getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getEffectiveAvatarUrl());
            embed.setTimestamp(Instant.now());
            embed.setDescription(url.toString());
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } catch (IOException e) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.removeMentions(ip) + " couldn't be resolved.").build()).queue();
        }
    }


    @Override
    public String getHelp() {
        return "Returns a Geo-IP Link of where the internet protocol leads to\n`" + Core.PREFIX + getInvoke() + " [IP]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "geoip";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"Geo Location"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}

 


