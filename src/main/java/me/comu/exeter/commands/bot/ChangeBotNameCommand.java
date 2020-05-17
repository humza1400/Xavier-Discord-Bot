package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChangeBotNameCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You aren't authorized to change the bot name").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a name").queue();
            return;
        }
        
        event.getJDA().getSelfUser().getManager().setName(args.get(0)).queue(
                v -> event.getChannel().sendMessage("Successfully changed bot name to " + args.get(0) + ".").queue(),
                t -> event.getChannel().sendMessage(" Failed to set bot name to " + args.get(0) + ".").queue());



    }

    @Override
    public String getHelp() {
        return "Changes the bot's name\n `" + Core.PREFIX + getInvoke() + " [name]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "changebotname";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"botname","setbotname"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
