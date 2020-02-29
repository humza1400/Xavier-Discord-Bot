package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class RateCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        int random = (int) (Math.random() * 100) + 1;
        if (args.isEmpty())
        {
            event.getChannel().sendMessage(String.format("**%s** rates themselves %s%% :flushed:", event.getMember().getEffectiveName(), random)).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone","everyone").replaceAll("@here","here")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(String.format("**%s** rates **%s** %s%%", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName(), random)).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(String.format("**%s** rates **%s** %s%%", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName(), random)).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Rates the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rate";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
