package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KillCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] kissUrls = {""};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(kissUrls[new Random().nextInt(kissUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** kills themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(kissUrls[new Random().nextInt(kissUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** kills **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
            return;
        }
        else if (!args.isEmpty() && !mentionedMembers.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(kissUrls[new Random().nextInt(kissUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** kills **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Kills the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kill";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"murder","slay"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
