package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PunchCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] punchUrls = {"https://media2.giphy.com/media/LdsJrFnANh6HS/giphy.gif","https://media0.giphy.com/media/arbHBoiUWUgmc/giphy.gif","https://media1.giphy.com/media/YjHx1taZwpfd6/source.gif","https://media.tenor.com/images/359a3a05dbde06a89cdcf494ad62bb5d/tenor.gif","https://media1.tenor.com/images/31686440e805309d34e94219e4bedac1/tenor.gif?itemid=4790446","https://media.giphy.com/media/8RsYgrFoQXPW0/giphy-downsized-large.gif","https://thumbs.gfycat.com/ImpeccableAdeptKentrosaurus-small.gif","https://media.tenor.com/images/1175cdf430e96e475d39777bced6798d/tenor.gif","https://thumbs.gfycat.com/ImperfectFrightenedFoal-size_restricted.gif","https://i.kym-cdn.com/photos/images/original/000/641/427/824.gif","https://media3.giphy.com/media/1426DjQ2dTFkzK/source.gif","https://i.kym-cdn.com/photos/images/original/001/117/646/bf9.gif","https://media1.giphy.com/media/10Im1VWMHQYfQI/source.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(punchUrls[new Random().nextInt(punchUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** punches themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(punchUrls[new Random().nextInt(punchUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** punches **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(punchUrls[new Random().nextInt(punchUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** punches **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Punches the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "punch";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"hit"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
