package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class MemberCountChannelCommand implements ICommand {

    public static boolean isVCSet = false;
    public static String vcID;
    public static boolean isMessageSet = false;
    public static String message;

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
        if (args.get(0).equalsIgnoreCase("message") || args.get(0).equalsIgnoreCase("msg"))
        {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            if (stringJoiner.toString().toLowerCase().contains(("{$count}"))) {
                message = stringJoiner.toString();
                isMessageSet = true;
                event.getChannel().sendMessage("Set count-message to: " + MarkdownUtil.monospace(stringJoiner.toString())).queue();
                return;
            } else {
                event.getChannel().sendMessage("Missing \"{$count}\" substitution").queue();
                return;
            }

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
        return "Sets the voice-channel that will display the member count (use {$count} to substitute counter in setting the message)\n`" + Core.PREFIX + getInvoke() + " [VC-ID]/[message] (message)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
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
