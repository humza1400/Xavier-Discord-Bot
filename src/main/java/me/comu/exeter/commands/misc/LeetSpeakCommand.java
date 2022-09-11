package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;


public class LeetSpeakCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please insert some text to leet-ify").build()).queue();
            return;
        }
        event.getMessage().delete().queue();
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        event.getChannel().sendMessageEmbeds(Utility.embed(stringJoiner.toString().replaceAll("A", "4").replaceAll("a", "4").replaceAll("E", "3").replaceAll("e", "3").replaceAll("I", "!").replaceAll("i", "!").replaceAll("O", "0").replaceAll("o", "0").replaceAll("S", "5").replaceAll("s", "5")).build()).queue();

    }

    @Override
    public String getHelp() {
        return "Makes your message hax0r like\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "leetspeak";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"1337speak", "leettalk", "1337talk", "leet", "1337"};
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
