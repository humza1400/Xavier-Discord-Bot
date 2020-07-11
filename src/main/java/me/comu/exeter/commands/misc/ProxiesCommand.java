package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ProxiesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a proxy-type (HTTP, HTTPS, SOCKS4, SOCKS5").queue();
            return;
        }
        String HTTP = "https://api.proxyscrape.com/?request=displayproxies&proxytype=http&timeout=1500";
        String HTTPS = "https://api.proxyscrape.com/?request=displayproxies&proxytype=https&timeout=1500";
        String SOCKS4 = "https://api.proxyscrape.com/?request=displayproxies&proxytype=socks4&timeout=1500";
        String SOCKS5 = "https://api.proxyscrape.com/?request=displayproxies&proxytype=socks5&timeout=1500";
        String method;
        String methodLink;
        if (args.get(0).equalsIgnoreCase("http")) {
            method = "HTTP Proxies\n";
            methodLink = HTTP;
        } else if (args.get(0).equalsIgnoreCase("https")) {
            method = "HTTPS Proxies\n";
            methodLink = HTTPS;
        } else if (args.get(0).equalsIgnoreCase("socks4")) {
            method = "SOCKS-4 Proxies\n";
            methodLink = SOCKS4;
        } else if (args.get(0).equalsIgnoreCase("socks5")) {
            method = "SOCKS-5 Proxies\n";
            methodLink = SOCKS5;
        } else {
            event.getChannel().sendMessage("Please specify a proxy-type (HTTP, HTTPS, SOCKS4, SOCKS5").queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder("**__" + method + "__**");
        try {
            final URL proxies = new URL(methodLink);
            final BufferedReader input = new BufferedReader(new InputStreamReader(proxies.openStream()));
            Stream<String> stream = input.lines().limit(20);
            stream.forEach(thing -> stringBuilder.append(thing).append("\n"));
            input.close();
            event.getChannel().sendMessage(new EmbedBuilder().setDescription(stringBuilder.toString()).setColor(Utility.getRandomColor()).build()).queue();
        } catch (Exception ignored) {
            event.getChannel().sendMessage("Something went wrong with connecting to the endpoint").queue();
        }


    }

    @Override
    public String getHelp() {
        return "Scrapes HTTP, HTTPS, SOCKS4, SOCKS5 proxies\n`" + Core.PREFIX + getInvoke() + " [type]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "proxies";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"scrape", "scrapeproxies", "proxy"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
