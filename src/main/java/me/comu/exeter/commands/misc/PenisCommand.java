package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PenisCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
       if (args.isEmpty()) {
           event.getChannel().sendMessageEmbeds(Utility.embedMessage(getPeePeeSize()).setColor(Core.getInstance().getColorTheme()).setTitle(event.getAuthor().getName() + " peepee size lol").build()).queue();
       }
       if (!event.getMessage().getMentionedMembers().isEmpty())
       {
           event.getChannel().sendMessageEmbeds(Utility.embedMessage(getPeePeeSize()).setColor(Core.getInstance().getColorTheme()).setTitle(event.getMessage().getMentionedMembers().get(0).getUser().getName() + " peepee size lol").build()).queue();
       } else {
           List<Member> members = event.getGuild().getMembersByName(args.get(0), true);
           if (members.isEmpty()) {
               event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
               return;
           }
           event.getChannel().sendMessageEmbeds(Utility.embedMessage(getPeePeeSize()).setColor(Core.getInstance().getColorTheme()).setTitle(members.get(0).getUser().getName() + " peepee size lol").build()).queue();
       }
    }

    private String getPeePeeSize()
    {
        int intSize = new Random().nextInt(16);
        String shaft = "=";
        return "8" + shaft.repeat(intSize) + "D";
    }


    @Override
    public String getHelp() {
        return "Tries to generate a discord nitro code.\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "penis";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"pene","dick","penissize","penislength","dicksize","dicklength","pp"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
