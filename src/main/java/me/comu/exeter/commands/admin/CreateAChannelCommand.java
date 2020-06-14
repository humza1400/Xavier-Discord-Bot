package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CreateAChannelCommand implements ICommand {

    public static final HashMap<String, String> map = new HashMap<>();
    private static HashMap<String, String> cacMap = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && Objects.requireNonNull(event.getMember()).getIdLong() != 699562509366984784L) {
            event.getChannel().sendMessage("You don't have permission to set the \"Create-A-Channel\" channel").queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a channel-id").queue();
            return;
        }
        try {
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(args.get(0));
            verifyCac(event.getGuild());
            cacMap.put(event.getGuild().getId(), Objects.requireNonNull(voiceChannel).getId());
            event.getChannel().sendMessage("Create-A-Channel Channel Successfully Set To `" + voiceChannel.getName() + "`").queue();
        } catch (NullPointerException | NumberFormatException ex) {
            event.getChannel().sendMessage("Invalid Channel-ID").queue();
        }

    }

    public static boolean isCacSet(Guild guild) {

        return cacMap.containsKey(guild.getId());

    }

    public static HashMap<String, String> getCacMap() {

        return cacMap;
    }

    public static void verifyCac(Guild guild) {
        if (isCacSet(guild))
            getCacMap().remove(guild.getId());
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
        return new String[]{"cac"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
