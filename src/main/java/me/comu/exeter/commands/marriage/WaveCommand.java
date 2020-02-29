package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WaveCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] waveUrls = {"https://media.tenor.com/images/f74374a8d12dbcd33c274dec139a3ff7/tenor.gif","https://media1.tenor.com/images/056c584d9335fcabf080ca43e583e3c4/tenor.gif?itemid=8994845","https://media1.tenor.com/images/72c9b849aa10b222371ebb99a6b1896a/tenor.gif?itemid=8807701","https://media.tenor.com/images/1cb934f643bc414084a90ca54a50cffa/tenor.gif","https://media.giphy.com/media/VUC9YdLSnKuJy/giphy.gif","https://i.imgur.com/DKT4tjt.gif","https://media.tenor.com/images/8b30352f7ce2e22e7f28044ce5d83813/tenor.gif","https://media.tenor.com/images/4b9b18c7aae49b108354a22a0cb615fc/tenor.gif","https://media2.giphy.com/media/4qQ8h34FSD5Xq/source.gif","https://media3.giphy.com/media/qR1fIvTV6hN16/source.gif","https://media.tenor.com/images/251d736302c3dcdb755b9aa59174556d/tenor.gif","https://i.kym-cdn.com/photos/images/original/001/254/725/261.gif","https://media2.giphy.com/media/yyVph7ANKftIs/source.gif","https://gifimage.net/wp-content/uploads/2017/09/anime-girl-waving-gif-1.gif","https://i.pinimg.com/originals/40/66/a5/4066a57e49c748d90330db89c15af609.gif","https://media.tenor.com/images/470b26ff14c3026c128cf0ce748bfc62/tenor.gif","https://gifimage.net/wp-content/uploads/2017/09/anime-waving-gif-13.gif","https://thumbs.gfycat.com/InconsequentialRipeLeopardseal-size_restricted.gif","https://media.tenor.com/images/73ce6a152fdf3fa2645f6153c646c9b7/tenor.gif","https://media3.giphy.com/media/9cZQnwdzUXTDG/source.gif","https://gifimage.net/wp-content/uploads/2017/09/anime-girl-waving-gif-13.gif","https://media3.giphy.com/media/9hEKnxxhpclbO/source.gif","https://66.media.tumblr.com/0ff48dce2689bd713c215bc6794ee479/tumblr_o328lujnMO1tydz8to1_540.gif","https://data.whicdn.com/images/95832389/original.gif","https://data.whicdn.com/images/308170254/original.gif","https://media1.tenor.com/images/d5e6472ab9473913382e82ecc298f1a2/tenor.gif?itemid=9810622","https://gifimage.net/wp-content/uploads/2017/09/anime-girl-waving-gif-12.gif","https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/aed18ec9-623e-48f7-82f3-616bb6cdbb7b/da4b1pt-5260aca1-7693-4a90-ab81-0c4b7bb70117.gif?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcL2FlZDE4ZWM5LTYyM2UtNDhmNy04MmYzLTYxNmJiNmNkYmI3YlwvZGE0YjFwdC01MjYwYWNhMS03NjkzLTRhOTAtYWI4MS0wYzRiN2JiNzAxMTcuZ2lmIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.yiYxC7grSkuyjv5ZQWs6QHPknJo_OLsJK41WYzpQHUg","https://data.whicdn.com/images/113790287/original.gif","https://media3.giphy.com/media/7TqC16j87YjU7sfEtj/source.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(waveUrls[new Random().nextInt(waveUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** waves at themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone","everyone").replaceAll("@here","here")).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedImage(waveUrls[new Random().nextInt(waveUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** waves at **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
            return;
        }
        else if (!args.isEmpty() && !mentionedMembers.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(waveUrls[new Random().nextInt(waveUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** waves at **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Waves at the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "wave";
    }

    @Override
    public String[] getAlias() {
        return new String[] {};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
