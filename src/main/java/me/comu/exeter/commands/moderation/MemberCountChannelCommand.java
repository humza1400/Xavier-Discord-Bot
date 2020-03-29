package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MemberCountChannelCommand implements ICommand {

    public static boolean isVCSet = false;
    public static String vcID;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to set the server stats channel").queue();
            return;
        }

        if (args.isEmpty())
        {
            event.getChannel().sendMessage("Please specify a valid voice-channel ID").queue();
            return;
        }
        try {
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(args.get(0));
                isVCSet = true;
                vcID = Objects.requireNonNull(voiceChannel).getId();
                event.getChannel().sendMessage("Servers stats channel set to `" + voiceChannel.getName() + "`").queue();
        } catch (NullPointerException ex)
        {
            event.getChannel().sendMessage("`" + args.get(0) + "` is not a valid voice channel ID.").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Sets the voice-channel that will display the member count\n`" + Core.PREFIX + getInvoke() + " [VC-ID]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "serverstats";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"sstats","setstatschannel"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
