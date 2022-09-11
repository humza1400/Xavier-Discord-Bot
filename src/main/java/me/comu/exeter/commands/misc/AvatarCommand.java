package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AvatarCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(event.getAuthor().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).build()).queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                try {
                    Member member = Objects.requireNonNull(event.getGuild().getMemberById(args.get(0)));
                    event.getChannel().sendMessageEmbeds(Utility.embedImage(member.getUser().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).build()).queue();
                } catch (NullPointerException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find a user matching that ID").build()).queue();
                    return;
                } catch (IllegalArgumentException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                    return;
                }
            }
            event.getChannel().sendMessageEmbeds(Utility.embedImage(targets.get(0).getUser().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).build()).queue();
        } else if (!memberList.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(memberList.get(0).getUser().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(Core.getInstance().getColorTheme()).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Returns the avatar of a designated user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "avatar";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"av"};
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
