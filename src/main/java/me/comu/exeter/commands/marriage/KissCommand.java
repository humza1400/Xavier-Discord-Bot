package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KissCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] kissUrls = {"https://media.discordyui.net/reactions/kiss/a2qzZ7w.gif","https://media.discordyui.net/reactions/kiss/cxl66EV.gif","https://media.discordyui.net/reactions/kiss/zAVG8ov.gif","https://media.discordyui.net/reactions/kiss/Xrxhow8.gif","https://media.discordyui.net/reactions/kiss/iJMycFA.gif","https://media.discordyui.net/reactions/kiss/BXtteTt.gif","https://media.discordyui.net/reactions/kiss/I6cANgF.gif","https://media.discordyui.net/reactions/kiss/yL5qOOP.gif","https://media.discordyui.net/reactions/kiss/GpyA4H5.gif","https://cdn.weeb.sh/images/rJ_U2p_Pb.gif","https://media1.tenor.com/images/d1a11805180742c70339a6bfd7745f8d/tenor.gif","https://media0.giphy.com/media/flmwfIpFVrSKI/giphy.gif","https://media3.giphy.com/media/mGAzm47irxEpG/giphy.gif","https://media1.giphy.com/media/nyGFcsP0kAobm/giphy.gif","https://media3.giphy.com/media/bm2O3nXTcKJeU/giphy.gif","https://media.tenor.com/images/9329859786d1e0772ba8e5bb9d108c9a/tenor.gif","https://media0.giphy.com/media/ll5leTSPh4ocE/giphy.gif","https://media1.giphy.com/media/LNwzxYKRlP0dzbLMpx/source.gif","https://media3.giphy.com/media/nO8kxVKdXSaek/giphy.gif","https://media1.giphy.com/media/Y9iiZdUaNRF2U/source.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(kissUrls[new Random().nextInt(kissUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** kisses themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(kissUrls[new Random().nextInt(kissUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** kisses **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
            return;
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(kissUrls[new Random().nextInt(kissUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** kisses **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Kisses the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kiss";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
