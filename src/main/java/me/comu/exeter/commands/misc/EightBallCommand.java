package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EightBallCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String[] answers = {"Yes.", "No.", "As I see it, yes.", "Ask again later.", "Better not tell you now.", "Cannot predict now.", "Concentrate and ask again.", "Don’t count on it.", "It is certain.", "It is decidedly so.", "Most likely.", "Probably.", "Probably Not.", "My reply is no.", "My sources say no.", "Outlook not so good.", "Outlook good.", "Reply hazy, try again.", "Signs point to yes.", "Very doubtful.", "Without a doubt.", "Yes – definitely.", "You may rely on it.", "Don't rely on it."};
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please ask a question").queue();
            return;
        }
        if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            // 0x277ecd cool blue color
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(answers[new Random().nextInt(answers.length)]).setColor(Core.getInstance().getColorTheme()).build()).queue();
        } else {
            event.getChannel().sendMessage(answers[new Random().nextInt(answers.length)]).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Ask the 8ball a yes or no question\n`" + Core.PREFIX + getInvoke() + " [question]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "8ball";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
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
