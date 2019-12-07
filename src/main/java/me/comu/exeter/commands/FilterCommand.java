package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class FilterCommand implements ICommand {

    private boolean filter;
    String[] LIST_OF_BAD_WORDS = {"anal", "anus", "arse", "ass", "motherfucker", "balls", "bastard", "bitch", "blowjob", "blow job", "buttplug","cock","coon","cunt","dildo","fag","dyke","fuck","fucking","nigger","Goddamn","jizz","nigga","pussy","shit","whore"};
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        filter = !filter;
        String temp;
        if (filter)
            temp = "true";
        else temp = "false";
        event.getChannel().sendMessage("Filter set to " + temp);
        String[] message = event.getMessage().getContentRaw().split(" ");
        if (filter == true) {
            for (int i = 0; i < message.length; i++) {
                boolean badWord = false;
                for (int b = 0; b < LIST_OF_BAD_WORDS.length; b++) {
                    if (message[i].equalsIgnoreCase(LIST_OF_BAD_WORDS[b])) {
                        event.getMessage().delete().queue();
                        badWord = true;
                            event.getChannel().sendMessage("No toxicity please! 💋" + event.getMember().getUser().getAsMention()).queue();

                    }
                }
                System.out.println(message[i] + " " + badWord);
            }
        }
    }

    @Override
    public String getHelp() {
        return "Toggles a text-channel filter on/off\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "filter";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"togglefilter"};
    }
}
