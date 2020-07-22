package me.comu.exeter.commands.marriage;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ShipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify who you'd like to ship").queue();
            return;
        }
        double chance = Utility.round(Math.random() * 100, 2);
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            List<Member> members = event.getMessage().getMentionedMembers();
            if (event.getMessage().getMentionedMembers().size() == 1) {
                if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                    event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").setImage(getShipImage(event.getAuthor().getEffectiveAvatarUrl(), members.get(0).getUser().getEffectiveAvatarUrl())).setTitle("Ship \u2764").setColor(Utility.getAmbientColor()).setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
                } else {
                    event.getChannel().sendMessage("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").queue();
                }
            } else {
                Member member1 = members.get(0);
                Member member2 = members.get(1);
                if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                    event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(member2.getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").setTitle("Ship \u2764").setImage(getShipImage(member1.getUser().getEffectiveAvatarUrl(), member2.getUser().getEffectiveAvatarUrl())).setColor(Utility.getAmbientColor()).setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
                } else {
                    event.getChannel().sendMessage("**" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(member2.getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").queue();
                }
            }
        } else {
            if (args.size() == 1) {
                List<Member> members = event.getGuild().getMembersByName(args.get(0), true);
                if (members.isEmpty()) {
                    event.getChannel().sendMessage("Couldn't find that member, try mentioning them instead").queue();
                } else {
                    if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                        event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").setImage(getShipImage(event.getAuthor().getEffectiveAvatarUrl(), members.get(0).getUser().getEffectiveAvatarUrl())).setTitle("Ship \u2764").setColor(Utility.getAmbientColor()).setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
                    } else {
                        event.getChannel().sendMessage("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").queue();
                    }
                }
            } else {
                List<Member> members1 = event.getGuild().getMembersByName(args.get(0), true);
                List<Member> members2 = event.getGuild().getMembersByName(args.get(1), true);
                if (!members1.isEmpty() && !members2.isEmpty()) {
                    if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                        event.getChannel().sendMessage(EmbedUtils.embedMessage("**" + Utility.removeMarkdown(members1.get(0).getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(members2.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").setImage(getShipImage(members1.get(0).getUser().getEffectiveAvatarUrl(), members2.get(0).getUser().getEffectiveAvatarUrl())).setTitle("Ship \u2764").setColor(Utility.getAmbientColor()).setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).build()).queue();
                    } else {
                        event.getChannel().sendMessage("**" + Utility.removeMarkdown(members1.get(0).getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(members2.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Couldn't find both members, try mentioning them instead").queue();
                }
            }
        }
    }

    private String getShipImage(String url, String url2) {
        return "https://api.alexflipnote.dev//ship?user=" + url + "&user2=" + url2;
    }


    @Override
    public String getHelp() {
        return "Ships yourself with the specified users or the two specified users together\n`" + Core.PREFIX + getInvoke() + " <user> [user2]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "ship";
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
