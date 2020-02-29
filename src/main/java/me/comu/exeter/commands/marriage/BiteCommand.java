package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BiteCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] biteUrls = {"https://media1.tenor.com/images/d97e4bc853ed48bf83386664956d75ec/tenor.gif?itemid=10364764","https://media1.giphy.com/media/iOGJlAYfB7O00/source.gif","https://media3.giphy.com/media/OqQOwXiCyJAmA/source.gif","https://media0.giphy.com/media/fhkRUj3BWmMnu/source.gif","https://media1.tenor.com/images/6b42070f19e228d7a4ed76d4b35672cd/tenor.gif?itemid=9051585","https://i.pinimg.com/originals/17/9a/16/179a16220f6cf2712073ccdc759ff3e1.gif","https://data.whicdn.com/images/151203966/original.gif","https://thumbs.gfycat.com/UniqueThickGalapagosalbatross-size_restricted.gif","https://media.tenor.com/images/616dcf3690e7edfac70c0e02c6d73559/tenor.gif","https://66.media.tumblr.com/7e2cad3ab0432205cdd5c37fab83d977/tumblr_ojh7gzPyeB1uzwbyjo1_400.gif","https://media2.giphy.com/media/Qoa2lJjbu2yw8/source.gif","https://i.gifer.com/MHg3.gif","https://media3.giphy.com/media/beGYe6qBhn9aU/giphy.gif","https://media3.giphy.com/media/FD7ETsGPEMV8c/giphy.gif","https://media3.giphy.com/media/ASeK6nCfqZXC8/source.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(biteUrls[new Random().nextInt(biteUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** bites themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
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
            event.getChannel().sendMessage(EmbedUtils.embedImage(biteUrls[new Random().nextInt(biteUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** bites **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(biteUrls[new Random().nextInt(biteUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** bites **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Bites the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "bite";
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
