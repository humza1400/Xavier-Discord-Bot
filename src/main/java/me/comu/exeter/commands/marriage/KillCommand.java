package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class KillCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] killUrls = {"https://media2.giphy.com/media/yGZnLLLmHVEB2/source.gif","https://media1.tenor.com/images/46051e203deaefc5642916c1eafa54a7/tenor.gif?itemid=3660367","https://media0.giphy.com/media/sxfw7IWQ8y3iE/source.gif","https://media0.giphy.com/media/23UYNxEnHywhy/source.gif","https://media2.giphy.com/media/eYAYL9QMjZyE0/source.gif","https://media3.giphy.com/media/JptUSbFQKCeo8/source.gif","https://media0.giphy.com/media/YDF98Gc6vekko/source.gif","https://i.makeagif.com/media/4-13-2015/hzdxbV.gif","https://thumbs.gfycat.com/WideeyedPossibleHammerkop-size_restricted.gif","https://thumbs.gfycat.com/ConventionalDarlingAnteater-size_restricted.gif","https://thumbs.gfycat.com/OffbeatDecimalAcornweevil-size_restricted.gif","https://media2.giphy.com/media/oyjLiqfcvJZdu/source.gif","https://thumbs.gfycat.com/EnergeticShortGalapagoshawk-small.gif","https://thumbs.gfycat.com/DaringDecisiveIcelandgull-max-1mb.gif","https://img.buzzfeed.com/buzzfeed-static/static/2015-04/29/14/enhanced/webdr10/anigif_enhanced-14452-1430333707-37.gif?crop=393:260;55,0&downsize=400"};
        String[] self = {"https://data.whicdn.com/images/283793301/original.gif","https://steamuserimages-a.akamaihd.net/ugc/916921405365241397/178D0EAA85A91BD538E9C3FD36FEDF37628410D8/","https://media.tenor.com/images/890d43fc28cd47f02b5b1adf399352fa/tenor.gif","https://media.giphy.com/media/saBjAxT3oCwq4/giphy.gif","https://media0.giphy.com/media/WsWJZcJoxmLUk/source.gif","https://steamuserimages-a.akamaihd.net/ugc/911288903785406241/9B6112ACC081EFBD100ACBFC825A005D5E4289C2/","https://steamuserimages-a.akamaihd.net/ugc/30729757403486268/7CF95EA2F7E98762EC187AD8EDFC3101C29E6764/","https://thumbs.gfycat.com/EquatorialGleefulArabianhorse-size_restricted.gif","https://media1.giphy.com/media/fGXDiRZvFVgJO/source.gif","https://media1.giphy.com/media/ru08UolwD4xRm/source.gif","https://i.gifer.com/Mbzr.gif","https://gifimage.net/wp-content/uploads/2017/07/anime-suicide-gif-23.gif","http://cdn.lowgif.com/full/b999e3148495d794-les-simpson-the-simpsons-homer-tentative-de-suicide-ratee.gif","https://media.tenor.com/images/6173c8831c2f4f2cad8804297ea2e552/tenor.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(self[new Random().nextInt(self.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** kills themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + Utility.removeMentions(args.get(0))).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(killUrls[new Random().nextInt(killUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** kills **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(killUrls[new Random().nextInt(killUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** kills **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Kills the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kill";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"murder","slay"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
