package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecreateChannelCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to nuke channels").queue();
            return;
        }
        event.getChannel().createCopy().queue(textChannel -> {
            event.getChannel().delete().queue();
            textChannel.getManager().setNSFW(event.getChannel().isNSFW()).setSlowmode(event.getChannel().getSlowmode()).setPosition(event.getChannel().getPosition()).queue();
            textChannel.sendMessage("`Channel Nuked by " + event.getAuthor().getAsMention() + "`").queue();
        });
    }

    @Override
    public String getHelp() {
        return "Destroys the current text-channel and recreates it\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "recreate";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"recreatechannel","nukechannel","nuke"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
