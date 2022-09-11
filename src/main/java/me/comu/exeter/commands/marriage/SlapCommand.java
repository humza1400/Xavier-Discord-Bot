package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SlapCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] slapUrls = {"https://media.discordyui.net/reactions/slap/RJHjyv3.gif","https://media.discordyui.net/reactions/slap/6QLFD1m.gif","https://media.discordyui.net/reactions/slap/4CZe0Jb.gif","https://media.discordyui.net/reactions/slap/aVDQEfA.gif","https://media.discordyui.net/reactions/slap/32TM2xW.gif","https://media.discordyui.net/reactions/slap/HGxqG1N.gif","https://media1.tenor.com/images/f9f121a46229ea904209a07cae362b3e/tenor.gif?itemid=7859254","https://media0.giphy.com/media/L7iHfUrBk3cqY/source.gif","https://media3.giphy.com/media/fNdolDfnVPKNi/source.gif","https://i.pinimg.com/originals/f8/5f/4c/f85f4c557e5a03d2a7a2e903b66e0047.gif","https://media1.tenor.com/images/3fd96f4dcba48de453f2ab3acd657b53/tenor.gif?itemid=14358509","https://i.pinimg.com/originals/4e/9e/a1/4e9ea150354ad3159339b202cbc6cad9.gif","https://i.kym-cdn.com/photos/images/newsfeed/000/940/326/086.gif","https://i.imgur.com/mdZR2D2.gif","https://media3.giphy.com/media/LeTDEozJwatvW/source.gif","https://image.myanimelist.net/ui/iFwdi4l06yUfakZDVoMBb8vtT-nSCBnddM-yee959XqOFiyXomKydVwy7vVhNX20xMF9nW-maaq_ZIVri75aZft3i5W47v9qEzqLTHQ6vTWHLEvg3Z--8A3hmfGkpOHKIEpsh9w16xgiFdfMkcncKA"};

        if (args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(slapUrls[new Random().nextInt(slapUrls.length)]).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** slaps themselves :flushed:", event.getMember().getEffectiveName())).build()).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if (!args.isEmpty() && mentionedMembers.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0)) + ".").build()).queue();
                return;
            } 
            event.getChannel().sendMessageEmbeds(Utility.embedImage(slapUrls[new Random().nextInt(slapUrls.length)]).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** slaps **%s**", event.getMember().getEffectiveName(), targets.get(0).getEffectiveName())).build()).queue();
        }
        else if (!args.isEmpty())
        {
            event.getChannel().sendMessageEmbeds(Utility.embedImage(slapUrls[new Random().nextInt(slapUrls.length)]).setColor(Core.getInstance().getColorTheme()).setTitle(String.format("**%s** slaps **%s**", event.getMember().getEffectiveName(), mentionedMembers.get(0).getEffectiveName())).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Slaps the specified user\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "slap";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"bitchslap"};
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
