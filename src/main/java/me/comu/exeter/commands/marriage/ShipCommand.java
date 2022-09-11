package me.comu.exeter.commands.marriage;

import me.comu.exeter.utility.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ShipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify who you'd like to ship").build()).queue();
            return;
        }
        double chance = Utility.round(Math.random() * 100, 2);
        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            List<Member> members = event.getMessage().getMentionedMembers();
            if (event.getMessage().getMentionedMembers().size() == 1) {
                if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                    try {
                        final HttpURLConnection con = (HttpURLConnection) new URL(getShipImage(event.getAuthor().getEffectiveAvatarUrl(), members.get(0).getUser().getEffectiveAvatarUrl())).openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("User-Agent", "Mozilla/5.0");
                        con.setRequestProperty("\u0041\u0075\u0074\u0068\u006f\u0072\u0069\u007a\u0061\u0074\u0069\u006f\u006e", "\u0049\u0056\u0036\u005f\u0071\u0061\u002d\u0035\u006a\u0037\u0050\u0046\u0070\u0046\u0041\u0034\u0075\u006c\u0062\u0078\u0064\u004a\u0066\u0057\u0075\u0056\u004d\u0058\u0058\u005a\u0078\u006d\u0075\u0037\u0053\u0047\u0050\u006b\u0044\u0031");
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setDescription("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!");
                        embed.setImage("attachment://swag-bot-ship.png");
                        embed.setTitle("Ship \u2764");
                        embed.setColor(Core.getInstance().getColorTheme());
                        embed.setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                        event.getChannel().sendFile(con.getInputStream(), "swag-bot-ship.png").setEmbeds(embed.build()).queue(lol -> Config.clearCacheDirectory(), lol1 -> Config.clearCacheDirectory());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                    }
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                }
            } else {
                Member member1 = members.get(0);
                Member member2 = members.get(1);
                if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                    try {
                    final HttpURLConnection con = (HttpURLConnection) new URL(getShipImage(member1.getUser().getEffectiveAvatarUrl(), member2.getUser().getEffectiveAvatarUrl())).openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    con.setRequestProperty("\u0041\u0075\u0074\u0068\u006f\u0072\u0069\u007a\u0061\u0074\u0069\u006f\u006e", "\u0049\u0056\u0036\u005f\u0071\u0061\u002d\u0035\u006a\u0037\u0050\u0046\u0070\u0046\u0041\u0034\u0075\u006c\u0062\u0078\u0064\u004a\u0066\u0057\u0075\u0056\u004d\u0058\u0058\u005a\u0078\u006d\u0075\u0037\u0053\u0047\u0050\u006b\u0044\u0031");
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setDescription("**" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(member2.getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!");
                    embed.setImage("attachment://swag-bot-ship.png");
                    embed.setTitle("Ship \u2764");
                    embed.setColor(Core.getInstance().getColorTheme());
                    embed.setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                    event.getChannel().sendFile(con.getInputStream(), "swag-bot-ship.png").setEmbeds(embed.build()).queue(lol -> Config.clearCacheDirectory(), lol1 -> Config.clearCacheDirectory());
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(member2.getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                    }
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(member1.getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(member2.getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                }
            }
        } else {
            if (args.size() == 1) {
                List<Member> members = event.getGuild().getMembersByName(args.get(0), true);
                if (members.isEmpty()) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find that member, try mentioning them instead").build()).queue();
                } else {
                    if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                        try {
                            final HttpURLConnection con = (HttpURLConnection) new URL(getShipImage(event.getAuthor().getEffectiveAvatarUrl(), members.get(0).getUser().getEffectiveAvatarUrl())).openConnection();
                            con.setRequestMethod("GET");
                            con.setRequestProperty("User-Agent", "Mozilla/5.0");
                            con.setRequestProperty("\u0041\u0075\u0074\u0068\u006f\u0072\u0069\u007a\u0061\u0074\u0069\u006f\u006e", "\u0049\u0056\u0036\u005f\u0071\u0061\u002d\u0035\u006a\u0037\u0050\u0046\u0070\u0046\u0041\u0034\u0075\u006c\u0062\u0078\u0064\u004a\u0066\u0057\u0075\u0056\u004d\u0058\u0058\u005a\u0078\u006d\u0075\u0037\u0053\u0047\u0050\u006b\u0044\u0031");
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setDescription("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!");
                            embed.setImage("attachment://swag-bot-ship.png");
                            embed.setTitle("Ship \u2764");
                            embed.setColor(Core.getInstance().getColorTheme());
                            embed.setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                            event.getChannel().sendFile(con.getInputStream(), "swag-bot-ship.png").setEmbeds(embed.build()).queue(lol -> Config.clearCacheDirectory(), lol1 -> Config.clearCacheDirectory());
                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                        }

                    } else {
                        event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(event.getAuthor().getAsTag()) + "** and **" + Utility.removeMarkdown(members.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                    }
                }
            } else {
                List<Member> members1 = event.getGuild().getMembersByName(args.get(0), true);
                List<Member> members2 = event.getGuild().getMembersByName(args.get(1), true);
                if (!members1.isEmpty() && !members2.isEmpty()) {
                    if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                        try {
                            final HttpURLConnection con = (HttpURLConnection) new URL(getShipImage(members1.get(0).getUser().getEffectiveAvatarUrl(), members2.get(0).getUser().getEffectiveAvatarUrl())).openConnection();
                            con.setRequestMethod("GET");
                            con.setRequestProperty("User-Agent", "Mozilla/5.0");
                            con.setRequestProperty("\u0041\u0075\u0074\u0068\u006f\u0072\u0069\u007a\u0061\u0074\u0069\u006f\u006e", "\u0049\u0056\u0036\u005f\u0071\u0061\u002d\u0035\u006a\u0037\u0050\u0046\u0070\u0046\u0041\u0034\u0075\u006c\u0062\u0078\u0064\u004a\u0066\u0057\u0075\u0056\u004d\u0058\u0058\u005a\u0078\u006d\u0075\u0037\u0053\u0047\u0050\u006b\u0044\u0031");
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setDescription("**" + Utility.removeMarkdown(members1.get(0).getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(members2.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!");
                            embed.setImage("attachment://swag-bot-ship.png");
                            embed.setTitle("Ship \u2764");
                            embed.setColor(Core.getInstance().getColorTheme());
                            embed.setFooter("Shipped by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl());
                            event.getChannel().sendFile(con.getInputStream(), "swag-bot-ship.png").setEmbeds(embed.build()).queue(lol -> Config.clearCacheDirectory(), lol1 -> Config.clearCacheDirectory());
                        } catch (Exception ex)
                        {
                            event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(members1.get(0).getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(members2.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                            ex.printStackTrace();
                        }

                    } else {
                        event.getChannel().sendMessageEmbeds(Utility.embed("**" + Utility.removeMarkdown(members1.get(0).getUser().getAsTag()) + "** and **" + Utility.removeMarkdown(members2.get(0).getUser().getAsTag()) + "** have a **" + chance + "%** of being shipped!").build()).queue();
                    }
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find both members, try mentioning them instead").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}