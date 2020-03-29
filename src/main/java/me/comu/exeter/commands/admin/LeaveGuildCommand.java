package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LeaveGuildCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("No permission").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Successfully left " + event.getGuild().getName()).queue();
            event.getGuild().leave().queue();
        }
        String id = args.get(0);
        try {
            Objects.requireNonNull(event.getJDA().getGuildById(id)).leave().queue();
        } catch (Exception ex) {event.getChannel().sendMessage("Invalid Guild Snowfalke").queue();}
        event.getChannel().sendMessage("Successfully left " + Objects.requireNonNull(event.getJDA().getGuildById(id)).getName()).queue();
    }

    @Override
    public String getHelp() {
        return "Leaves the guild the specified ID corresponds with\n`" + Core.PREFIX + getInvoke() + " [ID]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "leaveserver";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"leaveguild", "gleave", "sleave"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
