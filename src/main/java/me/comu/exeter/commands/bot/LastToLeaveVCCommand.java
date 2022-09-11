package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LastToLeaveVCCommand implements ICommand {

    private HashMap<String, ArrayList<String>> lastVc = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a Channel-ID.").build()).queue();
            return;
        }
        VoiceChannel voiceChannel = event.getJDA().getVoiceChannelById(args.get(0));
        if (voiceChannel == null) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I couldn't find a voice-channel matching that ID.").build()).queue();
        } else {
            if (voiceChannel.getMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I can't start a Last-to-Leave-VC Event with zero people in the voice chat.").build()).queue();
                return;
            } else {
                for (Member member : voiceChannel.getMembers()) {
                    if (voiceChannel.getMembers().size() == 1 && member.getUser().isBot()) {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I can't start a Last-to-Leave-VC Event with a bot in the voice-channel with no real users.").build()).queue();
                        return;
                    }
                }
            }
            if (lastVc.containsKey(event.getGuild().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There's already a Last-to-Leave-VC Event going on, I've terminated that event and started the new event in `").build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(Utility.embed("Successfully started a Last-to-Leave-VC Event in `" + voiceChannel.getName() + "`.").build()).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "Creates a Last-to-Leave-VC Event in the specified channel\n" + "`" + Core.PREFIX + getInvoke() + " [Channel-ID]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lasttoleavevc";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"last2leavevc", "last2leave", "lasttoleave", "vclast", "lastvc"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
