package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScrapeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify what protocol scrape you would like to use.").queue();
            return;
        }
        List<Member> memberList = event.getGuild().getMembers();
        StringBuilder memberName = new StringBuilder();
        int memberCount = 0;
        try {
            for (Member m : memberList) {
                memberCount = memberList.size();
                memberName.append(m.getEffectiveName()).append('#').append(m.getUser().getDiscriminator()).append(", ");
            }
            event.getMessage().delete().queue();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Scraper Type");
            embedBuilder.setDescription("Vanilla Scraper");
            embedBuilder.addField("Queued Scraped Users | (" +memberCount +")", args.get(0), true);
            embedBuilder.addField("Users:","[" + memberName + "]", true);
            embedBuilder.setColor(0x521e8a);
            embedBuilder.setFooter("Scraped By " + Objects.requireNonNull(event.getMember()).getUser().getAsTag(), event.getMember().getUser().getAvatarUrl());
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            long time = System.currentTimeMillis();
            event.getChannel().sendMessage("A;; users scraped to `Swag's Test Server` in `#ilovemen`").queue(response -> response.editMessageFormat("users scraped to `Swag's Test Server` in `#ilovemen` (%d ms)" , System.currentTimeMillis() - time).queue());
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
        return "scrapeusers";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
