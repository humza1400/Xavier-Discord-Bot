package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CreateAChannelCommand implements ICommand {

    public static final HashMap<String, String> vcMap = new HashMap<>();
    private static final HashMap<String, String> guildCacMap = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set the \"Create-A-Channel\" channel.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR))
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permission to \"Create-A-Channel\".").build()).queue();
            return;
        }


        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a Channel-Id.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("clear") || args.get(0).equalsIgnoreCase("null"))
        {
            guildCacMap.remove(event.getGuild().getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("Removed the Create-A-Channel VC.").build()).queue();
            return;
        }
        try {
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(args.get(0));
            guildCacMap.put(event.getGuild().getId(), Objects.requireNonNull(voiceChannel).getId());
            event.getChannel().sendMessageEmbeds(Utility.embed("Create-A-Channel Channel Successfully Set To `" + voiceChannel.getName() + "`.").build()).queue();
        } catch (NullPointerException | NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid Channel-ID.").build()).queue();
        }

    }

    public static boolean isCacSet(Guild guild) {

        return guildCacMap.containsKey(guild.getId());

    }

    public static HashMap<String, String> getGuildCacMap() {

        return guildCacMap;
    }


    @Override
    public String getHelp() {
        return "Sets the specified channel as the \"Create-A-Channel\" channel\n`" + Core.PREFIX + getInvoke() + " [channel-id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
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

    @Override
    public boolean isPremium() {
        return true;
    }
}
