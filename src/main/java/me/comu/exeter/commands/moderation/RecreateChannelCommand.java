package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecreateChannelCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to nuke channels").build()).queue();
            return;
        }
        event.getChannel().createCopy().queue(textChannel -> {
            int position = event.getChannel().getPosition();
            event.getChannel().delete().queue();
            textChannel.getManager().setNSFW(event.getChannel().isNSFW()).setSlowmode(event.getChannel().getSlowmode()).setPosition(position).queue();
            textChannel.sendMessageEmbeds(Utility.embed(":bomb: `Channel Nuked by` " + event.getAuthor().getAsMention()).build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
