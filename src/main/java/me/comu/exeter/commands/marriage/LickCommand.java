package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LickCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] lickUrls = {"https://media1.tenor.com/images/efd46743771a78e493e66b5d26cd2af1/tenor.gif?itemid=14002773","https://i.imgur.com/YG4i71E.gif","https://media.giphy.com/media/8GiREm7aqMwN2/200.gif","https://media1.tenor.com/images/c4f68fbbec3c96193386e5fcc5429b89/tenor.gif?itemid=13451325","https://i.gifer.com/CAmE.gif","https://i.imgur.com/uALJJV2.gif","https://i.imgur.com/ZbAcxet.gif?noredirect","https://media0.giphy.com/media/11o44A5ZoR4cZq/source.gif","https://i.imgur.com/mCLNteg.gif","https://media1.tenor.com/images/fc0ef2ba03d82af0cbd6c5815c3c83d5/tenor.gif?itemid=12141725","https://media1.giphy.com/media/E0bP09t5c9SDu/source.gif","https://media2.giphy.com/media/AdbuzBaEVJsyI/giphy.gif","https://i.kym-cdn.com/photos/images/newsfeed/001/334/768/fbc.gif","https://66.media.tumblr.com/e8d3d11d3cda003ed6382528167804f7/tumblr_okpgy1wPnn1u3g0bjo1_400.gif","https://media.giphy.com/media/ky2p36qednUu4/giphy.gif","https://pa1.narvii.com/7150/ec41cd480f0424b8f89eb907f608e51a5374b263r1-960-540_hq.gif","https://i.gifer.com/MLSy.gif","https://i.kym-cdn.com/photos/images/original/000/995/417/60f.gif","https://thumbs.gfycat.com/GeneralFarBuckeyebutterfly-size_restricted.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(lickUrls[new Random().nextInt(lickUrls.length)]).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** licks themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0)) + ".").build()).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Multiple users found! Try mentioning the user instead.").build()).queue();
                return;
            }
            event.getChannel().sendMessageEmbeds(Utility.embedImage(lickUrls[new Random().nextInt(lickUrls.length)]).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** licks **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(lickUrls[new Random().nextInt(lickUrls.length)]).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** licks **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Licks the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "lick";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
