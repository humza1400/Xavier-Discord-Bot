package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SexCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] sexUrls = {"https://zippy.gfycat.com/PersonalSeparateAsianconstablebutterfly.gif","https://s3-us-west-1.amazonaws.com/porngifs/img/37243","https://31.media.tumblr.com/e856023f334f4ce4b46581d864fa8242/tumblr_n185r57dTH1s20ivko1_500.gif","https://cdnio.luscious.net/tiagobr5/347158/momiji-inubashiri-levando-gostoso-touhou_01DX96T8HRW9ZCCYH3QSFPY001.gif","https://i.pinimg.com/originals/b8/7f/0a/b87f0a54c3a7748029326f6238009637.gif","https://xxgasm.com/wp-content/upload/2016/05/hentai_pee_pa-171.gif","https://www.migrations-sante.eu/image/531442.gif","https://us.rule34.xxx//images/3041/fef7e4c3dd3da137c8947588490b8210.gif","https://68.media.tumblr.com/72a172daeeb069578b97c57ef7dc8b7a/tumblr_mzz37qUGVu1t90djyo1_1280.gif"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(sexUrls[new Random().nextInt(sexUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** fucks themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
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
            event.getChannel().sendMessage(EmbedUtils.embedImage(sexUrls[new Random().nextInt(sexUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** fucks **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessage(EmbedUtils.embedImage(sexUrls[new Random().nextInt(sexUrls.length)]).setColor(event.getMember().getColor()).setTitle(String.format("**%s** rapes **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "**NSFW** Fucks the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "fuck";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"sex","rape"};
    }

    @Override
    public Category getCategory() {
        return Category.MARRIAGE;
    }
}
