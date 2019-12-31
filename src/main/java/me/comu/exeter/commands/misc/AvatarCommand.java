package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AvatarCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage(EmbedUtils.embedImage(String.format(event.getAuthor().getEffectiveAvatarUrl().concat("?size=256&f=.gif"))).setColor(event.getMember().getColor()).build()).queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty())
            {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (targets.size() > 1)
            {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(targets.get(0).getUser().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(event.getMember().getColor()).build()).queue();
        } else if (!memberList.isEmpty()){
            event.getChannel().sendMessage(EmbedUtils.embedImage(memberList.get(0).getUser().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(event.getMember().getColor()).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Returns the avatar of a designated user\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "avatar";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"av","pfp"};
    }

     @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
