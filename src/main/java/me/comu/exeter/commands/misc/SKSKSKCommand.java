package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SKSKSKCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("my analytics show that you are a blackie, therefore i have blocked you from executing this command").queue();
            return;
        }
        if (mentionedMembers.isEmpty()) {
            event.getMessage().delete().queue();
            event.getChannel().sendMessage("AND I OOP-").queue();
            for (int i = 0; i < 6; i++) {
                event.getChannel().sendMessage("sksksk :wink:").queue();
            }
        } else {
            event.getMessage().delete().queue();
            event.getChannel().sendMessage("AND I OOP-").queue();
            for (int i = 0; i < 6; i++) {
                event.getChannel().sendMessage("skskks :wink:" + mentionedMembers.iterator().next().getAsMention()).queue();
            }
        }
    }


    @Override
    public String getHelp() {
        return "SKSKSK's a user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "sksksk";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
