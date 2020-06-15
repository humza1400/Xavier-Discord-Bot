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

public class HugCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] hugUrls = {"https://media.discordyui.net/reactions/hug/kpMgkn1.gif","https://media2.giphy.com/media/iGWtm03BldqU0/source.gif","https://media.discordyui.net/reactions/hug/Z8eQKOZ.gif","https://media.discordyui.net/reactions/hug/N4CPwjr.gif","https://media.discordyui.net/reactions/hug/vcQm1YL.gif","https://media.discordyui.net/reactions/hug/2qlVdBY.gif","https://media.discordyui.net/reactions/hug/WgdUyyJ.gif","https://media.discordyui.net/reactions/hug/JwU3EPy.gif","https://media.discordyui.net/reactions/hug/fP0FnXi.gif","https://media.discordyui.net/reactions/hug/6bJxUOb.gif","https://media.discordyui.net/reactions/hug/f4BRs7v.gif","https://media.discordyui.net/reactions/hug/S5rnKCF.gif","https://i.pinimg.com/originals/f2/80/5f/f2805f274471676c96aff2bc9fbedd70.gif","https://media3.giphy.com/media/lrr9rHuoJOE0w/giphy.gif","https://media2.giphy.com/media/od5H3PmEG5EVq/giphy.gif","https://media2.giphy.com/media/qscdhWs5o3yb6/giphy.gif","https://i.pinimg.com/originals/f2/e6/7f/f2e67f04b6a0ab30870011bd17190409.gif","https://media3.giphy.com/media/aD1fI3UUWC4/source.gif","https://66.media.tumblr.com/8d7f21698a2e2c85bf9ff7a829488336/tumblr_nmrmhleuYw1u4zujko1_400.gif","https://data.whicdn.com/images/270929601/original.gif","https://66.media.tumblr.com/607bc7d527ba0dff8e16f27f839da02d/tumblr_msii46Qegu1sgjyb4o1_400.gif","http://25.media.tumblr.com/tumblr_ma7l17EWnk1rq65rlo1_500.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(hugUrls[new Random().nextInt(hugUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** hugs themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
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
            event.getChannel().sendMessage(EmbedUtils.embedImage(hugUrls[new Random().nextInt(hugUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** hugs **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(hugUrls[new Random().nextInt(hugUrls.length)]).setColor(Objects.requireNonNull(event.getMember()).getColor()).setTitle(String.format("**%s** hugs **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Hugs the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "hug";
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
