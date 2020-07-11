package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ReverseGoogleSearchCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert an image-link or mention a user").queue();
            return;
        }
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("https://images.google.com/searchbyimage?image_url=" + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl()).queue();
        } else {
            if (event.getGuild().getMembersByName(args.get(0), true).isEmpty()) {
                if (args.get(0).toLowerCase().endsWith("png") || args.get(0).toLowerCase().endsWith("jpg") || args.get(0).toLowerCase().endsWith("jpeg") || args.get(0).toLowerCase().endsWith("gif"))
                    event.getChannel().sendMessage("https://images.google.com/searchbyimage?image_url=" + Utility.removeMentions(args.get(0))).queue();
                else
                    event.getChannel().sendMessage("I couldn't resolve an image in that link, try again :|").queue();
            } else {
                event.getChannel().sendMessage("https://images.google.com/searchbyimage?image_url=" + event.getGuild().getMembersByName(args.get(0), true).get(0).getUser().getEffectiveAvatarUrl()).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Reverse google searches links or users pfp. (PNG, JPG, JPEG, GIF)\n`" + Core.PREFIX + getInvoke() + " [image-link]/[user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "reversesearch";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"revsearch", "reversegooglesearch", "searchgoogle", "anticatfish", "catfish"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
