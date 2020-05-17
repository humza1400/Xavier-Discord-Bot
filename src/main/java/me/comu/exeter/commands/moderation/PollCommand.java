package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class PollCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() < 2) {
            event.getChannel().sendMessage("Please insert two options and an optional message").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String rawMessage = stringJoiner.toString();
        String message;
        if (!rawMessage.contains("msg:"))
            message = "React to cast your vote!";
        else
            message = rawMessage.substring(rawMessage.indexOf("msg:") + 4).replaceFirst(rawMessage.substring(rawMessage.indexOf("1:")),"");
        String option2 = rawMessage.substring(rawMessage.indexOf("2:") + 2).replaceFirst(message, "").replaceFirst("msg:", "");
        String option1 = rawMessage.substring(rawMessage.indexOf("1:") + 2).replace(option2, "").replaceFirst(message,"").replaceFirst("msg:","").replaceFirst("2:","");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(event.getGuild().getName() + " Poll!");
        embedBuilder.setDescription(message);
        embedBuilder.addField("Option 1", option1, false);
        embedBuilder.addField("Option 2", option2, false);
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setFooter("Poll created by " + Objects.requireNonNull(event.getMember()).getUser().getAsTag(), event.getMember().getUser().getAvatarUrl());
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(embedBuilder.build()).queue((message1) -> {
                    message1.addReaction("\u0031\u20E3").queue();
                    message1.addReaction("\u0032\u20E3").queue();
                }
        );
        embedBuilder.clear();
        event.getMessage().delete().queue();

    }

    @Override
    public String getHelp() {
        return "Creates a poll\n`" + Core.PREFIX + getInvoke() + " 1:[option-1] 2:[option-2] msg:<message>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "poll";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"createpoll"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
