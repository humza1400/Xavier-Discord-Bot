package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AvatarCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (event.getMessage().getMentionedMembers().isEmpty()) {
           // MessageEmbed embed = new EmbedBuilder().setColor(event.getMember().getColor()).setThumbnail(event.getAuthor().getEffectiveAvatarUrl().concat("?size=258&f=.gif")).build();
           // event.getChannel().sendMessage(EmbedUtils.embedImage(String.format("https://cdn.discordapp.com/avatars/175728291460808706/a_87d2a28833c1618bf26e31c4d6897109.gif?size=256&f=.gif")).setColor(event.getMember().getColor()).build()).queue();
            event.getChannel().sendMessage(EmbedUtils.embedImage(event.getAuthor().getEffectiveAvatarUrl().concat("?size=256&f=.gif")).setColor(event.getMember().getColor()).build()).queue();

            //event.getChannel().sendMessage(embed).queue();
        }
        else {
           // MessageEmbed embed = new EmbedBuilder().setColor(memberList.get(0).getColor()).setThumbnail(memberList.get(0).getUser().getEffectiveAvatarUrl()).build();
            event.getChannel().sendMessage(EmbedUtils.embedImage(String.format(memberList.get(0).getUser().getEffectiveAvatarUrl().concat("?size=256&f=.gif"))).setColor(event.getMember().getColor()).build()).queue();
//            event.getChannel().sendMessage(embed).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Returns the avatar of a designated user\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "avatar";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"av","pfp"};
    }
}
