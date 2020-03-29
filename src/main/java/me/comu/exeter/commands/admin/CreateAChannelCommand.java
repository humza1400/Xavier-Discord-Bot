package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CreateAChannelCommand implements ICommand {

    public static boolean isSet = false;
    public static String channelID = null;
    public static String guildID = null;
    public static final HashMap<String, String> map = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to set the \"Create-A-Channel\" channel").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a channel-id").queue();
            return;
        }
        try {
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(args.get(0));
            channelID = Objects.requireNonNull(voiceChannel).getId();
            guildID = event.getGuild().getId();
            voiceChannel.getManager().setName("Create-A-Channel").queue();
            event.getChannel().sendMessage("Create-A-Channel Channel Successfully Set To `" + voiceChannel.getName() + "`").queue();
            isSet = true;
        } catch (NullPointerException | NumberFormatException ex) {
            event.getChannel().sendMessage("Invalid Channel-ID").queue();
    }

    }

    @Override
    public String getHelp() {
        return "Sets the specified channel as the \"Create-A-Channel\" channel\n`" + Core.PREFIX + getInvoke() + "` [channel-id]\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "createachannel";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"cac"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
