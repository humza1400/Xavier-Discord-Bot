package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class BannerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty() || event.getMessage().getMentionedMembers().isEmpty()) {
            event.getAuthor().retrieveProfile().queue(user -> {
                if (user.getBannerUrl() == null) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("You have no banner set.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embedImage(user.getBannerUrl().concat("?size=256&f=.gif")).setTitle("Your Banner").setColor(Core.getInstance().getColorTheme()).build()).queue();
                }
            });
        } else {
            User user = event.getMessage().getMentionedMembers().get(0).getUser();
            if (user.isBot()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Bot's don't have banners dummy.").build()).queue();
                return;
            }
            user.retrieveProfile().queue(user1 -> {
                if (user1.getBannerUrl() == null) {
                    event.getChannel().sendMessageEmbeds(Utility.embed(user.getAsMention() + " has no banner set.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embedImage(user1.getBannerUrl().concat("?size=256&f=.gif")).setTitle(user.getAsTag() + "'s Banner").setColor(Core.getInstance().getColorTheme()).build()).queue();
                }
            });
        }
    }

    @Override
    public String getHelp() {
        return "Returns the user's banner\n`" + Core.PREFIX + getInvoke() + " <user>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "banner";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"userbanner"};
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
