package me.comu.exeter.commands.image;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TriggeredImageCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String url = "https://some-random-api.ml/canvas/triggered?avatar=";
        if (args.isEmpty() && event.getMessage().getAttachments().isEmpty()) {
            url = url + event.getAuthor().getEffectiveAvatarUrl();
        }
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            url = url + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl();
        } else if (!args.isEmpty() && !Utility.isUrl(args.get(0))) {
            event.getChannel().sendMessage("I couldn't resolve the specified URL").queue();
            return;
        }

        if (!args.isEmpty() && Utility.isUrl(args.get(0))) {
            url = url + args.get(0);
        } else if (!event.getMessage().getAttachments().isEmpty()) {
            url = url + event.getMessage().getAttachments().get(0).getUrl();
        }
        try {
            Utility.saveGif(url, "cache", "downloaded");
            File file = new File("cache/downloaded.gif");
            event.getChannel().sendFile(file, "swag.gif").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            ex.printStackTrace();
            Config.clearCacheDirectory();
            event.getChannel().sendMessage("Something went wrong, try again later").queue();
        }


    }


    @Override
    public String getHelp() {
        return "Triggers the user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "trigger";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"triggered"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}