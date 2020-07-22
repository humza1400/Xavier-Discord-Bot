package me.comu.exeter.commands.image;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TrashImageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getMentionedMembers().size() != 1)
        {
            event.getChannel().sendMessage("You need to mention a user to trash!").queue();
        }
        final String api = "https://api.alexflipnote.dev/trash?face=" + Objects.requireNonNull(event.getMember()).getUser().getEffectiveAvatarUrl() + "&trash=" + event.getMessage().getMentionedMembers().get(0).getUser().getEffectiveAvatarUrl();
        try {

            BufferedImage img = ImageIO.read(new URL(api));
            File file = new File("cache/downloaded.png");
            ImageIO.write(img, "png", file);
            event.getChannel().sendFile(file, "swag.png").queue(lol -> Config.clearCacheDirectory());
        } catch (Exception ex) {
            Config.clearCacheDirectory();
            ex.printStackTrace();
            event.getChannel().sendMessage("Something went wrong when interpolating the image.").queue();
        }

    }

    @Override
    public String getHelp() {
        return "Puts the specified user in the trash\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "trash";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"trashuser"};
    }

    @Override
    public Category getCategory() {
        return Category.IMAGE;
    }
}
