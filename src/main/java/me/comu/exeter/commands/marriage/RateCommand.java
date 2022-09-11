package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RateCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        int random = (int) (Math.random() * 100) + 1;
        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("**%s** rates themselves %s%% :flushed:", Objects.requireNonNull(event.getMember()).getEffectiveName(), random)).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0)) + ".").build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("**%s** rates **%s** %s%%", Objects.requireNonNull(event.getMember()).getEffectiveName(), targets.get(0).getEffectiveName(), random)).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embed(String.format("**%s** rates **%s** %s%%", Objects.requireNonNull(event.getMember()).getEffectiveName(), mentionedMembers.get(0).getEffectiveName(), random)).build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
