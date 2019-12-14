package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScrapeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify what protocol scrape you would like to use.").queue();
            return;
        }
        List<Member> memberList = event.getGuild().getMembers();
        String memberName = "";
        int memberCount = 0;
        try {
            for (Member m : memberList) {
                memberCount = memberList.size();
                memberName += m.getEffectiveName() + '#' + m.getUser().getDiscriminator() + ", ";
            }
            List<Message> messages2 = event.getChannel().getHistory().retrievePast(2).complete();
            messages2.get(0).delete().queueAfter(3, TimeUnit.MILLISECONDS);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Scraper Type");
            embedBuilder.setDescription("Vanilla Scraper");
            embedBuilder.addField("Queued Scraped Users | (" +memberCount +")", args.get(0), true);
            embedBuilder.addField("Users:","[" + memberName + "]", true);
            embedBuilder.setColor(0x521e8a);
            embedBuilder.setFooter("Scraped By " + event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl());
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            long time = System.currentTimeMillis();
            event.getChannel().sendMessage(Core.DEBUG + "A;; users scraped to `Comu's Test Server` in `#ilovemen`").queue(response -> {
                response.editMessageFormat(Core.DEBUG + "users scraped to `Comu's Test Server` in `#ilovemen` (%d ms)" , System.currentTimeMillis() - time).queue();});
            embedBuilder.clear();
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("Value cannot be longer than 1024 characters!").queue();
        }


    }

    @Override
    public String getHelp() {
        return "Scrapes all users in the discord\n`" + Core.PREFIX + getInvoke() + "` [argument]\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "scrape";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
