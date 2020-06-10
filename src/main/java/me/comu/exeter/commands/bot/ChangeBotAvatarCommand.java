package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChangeBotAvatarCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You aren't authorized to change the bot's avatar").queue();
            return;
        }
        String url;
        if (args.isEmpty())
            if (!event.getMessage().getAttachments().isEmpty() && event.getMessage().getAttachments().get(0).isImage())
                url = event.getMessage().getAttachments().get(0).getUrl();
            else
                url = null;
        else
            url = args.get(0);
        InputStream s = Wrapper.imageFromUrl(url);
        if (s == null) {
            event.getChannel().sendMessage("Failed to change the bot's avatar").queue();
        } else {
            try {
                event.getJDA().getSelfUser().getManager().setAvatar(Icon.from(s)).queue(
                        v -> event.getChannel().sendMessage("Successfully changed the bot's avatar").queue(),
                        t -> event.getChannel().sendMessage("Failed to change the bot's avatar").queue());
            } catch (IOException e) {
                event.getChannel().sendMessage("Failed to load the bot's avatar").queue();
            }
        }

    }

    @Override
    public String getHelp() {
        return "Changes the bot's avatar\n `" + Core.PREFIX + getInvoke() + " [link]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "changebotavatar";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"botavatar", "changebotpfp", "botpfp", "setbotpfp", "setbotavatar"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
