package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HoldHandsCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] holdHandsUrls = {"https://media.giphy.com/media/w7RGPBLGO8rjq/giphy.gif","https://media1.tenor.com/images/9d921ae2f69420beb68dcf083d7e0f43/tenor.gif?itemid=14913237","https://i.pinimg.com/originals/02/29/b3/0229b33cbea8b3c3f431ca309fb4e597.gif","https://media1.tenor.com/images/200dbf625af7ea0014ad2e641a246018/tenor.gif?itemid=13925441","https://i.pinimg.com/originals/64/72/2f/64722f8fe88db6c1178fbae5ff6cd06e.gif","https://media1.giphy.com/media/3o7ZeSQEPtgrYCGu0U/giphy.gif","https://66.media.tumblr.com/9ca6db92792d43df5875018dd48c8e11/tumblr_o5154bIo1e1uapp8co1_400.gif","https://31.media.tumblr.com/b2d6fadf3ac3943224ad65a87db2d6be/tumblr_mrddx4R7Tu1sorll2o1_500.gif","https://66.media.tumblr.com/a6be2d9fbc4cf8051fbf809e2a7bd440/tumblr_pbcvluPnwB1xvwkewo1_400.gif","https://carnivorouslreviews.files.wordpress.com/2018/08/interlocking.gif","https://i.gifer.com/Y6gi.gif","https://media2.giphy.com/media/9Gc6YRHLl09BS/source.gif","https://33.media.tumblr.com/49c0a5d1077adc1581dee31e1b41580b/tumblr_mtjpeifYF91su612oo1_500.gif","https://media1.giphy.com/media/357GyFz8sZsuQ/giphy.gif","https://media2.giphy.com/media/Kp3XXgM8KaMI8/source.gif","https://gifimage.net/wp-content/uploads/2018/11/holding-hands-gif-anime-3.gif","https://thumbs.gfycat.com/InfatuatedEnergeticCockatoo-small.gif","https://media1.tenor.com/images/aa76186dc654f6938bd2e75dc0aa2a0b/tenor.gif?itemid=10316502","https://i.pinimg.com/originals/8f/70/71/8f70714a8fc965fdcae4d7d11bc4c683.gif","https://66.media.tumblr.com/2bee459292242f4c7716a85c6ee7d631/tumblr_inline_oxql732zvw1rgn573_540.gif","https://i.imgur.com/A9kxW4y.gif","https://pa1.narvii.com/6274/fd47cb404535d3891e33572c92d6e9cd5833c8d1_hq.gif","https://i.gifer.com/SvJh.gif","https://i.pinimg.com/originals/14/4b/a0/144ba0b7a2d8632d82524e7237ecd4e2.gif","https://media3.giphy.com/media/LEtBecwM1JWLe/source.gif",""};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(holdHandsUrls[new Random().nextInt(holdHandsUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** holds his own hands :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(holdHandsUrls[new Random().nextInt(holdHandsUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** holds **%s**'s hands", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(holdHandsUrls[new Random().nextInt(holdHandsUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** holds **%s**'s hands", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Holds the specified user's hand\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "holdhand";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"holdhands","hh"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
