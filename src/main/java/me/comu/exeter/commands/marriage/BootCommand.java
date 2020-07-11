package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BootCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] bootUrls = {"https://media0.giphy.com/media/wOly8pa4s4W88/source.gif","https://media.giphy.com/media/u2LJ0n4lx6jF6/giphy.gif","https://media1.tenor.com/images/15a74d10bb6dce11476acfdefe614540/tenor.gif?itemid=7779674","https://i.kym-cdn.com/photos/images/newsfeed/001/053/707/042.gif","https://media.giphy.com/media/VgR9ky7IYjc9W/giphy.gif","https://media0.giphy.com/media/5IPhXGZWeRk64/source.gif","https://i.imgur.com/24yKIai.gif","https://media.giphy.com/media/3ohhwCqLY68iA1kHRe/giphy.gif","https://media3.giphy.com/media/f5HJLMJjp55HW/giphy.gif","https://media0.giphy.com/media/qtnmKoDSUGtKU/source.gif","https://media2.giphy.com/media/ewp6KdCOEMSTm/source.gif","https://i.pinimg.com/originals/3f/44/1b/3f441ba8a891512172fa1861af7dbedc.gif","https://24.media.tumblr.com/dfa5b815c6e587c2f1581d85c7c85442/tumblr_mw4irwOMjK1solyeco1_500.gif","https://media.giphy.com/media/11sctbYIQPR280/giphy-downsized-large.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(bootUrls[new Random().nextInt(bootUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** boots themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + Utility.removeMentions(args.get(0))).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(bootUrls[new Random().nextInt(bootUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** boots **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(bootUrls[new Random().nextInt(bootUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** boots **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Boots the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "boot";
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
