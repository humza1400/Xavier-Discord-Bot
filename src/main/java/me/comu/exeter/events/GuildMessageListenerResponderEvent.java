package me.comu.exeter.events;

import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildMessageListenerResponderEvent extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
//        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String msg = event.getMessage().getContentRaw().toLowerCase();
        if (!event.getMessage().getAuthor().isBot()) {
//            if (msg.contains("andrew") || msg.contains("andrw") || msg.contains("<@447803380958756864>")) {
//                event.getChannel().sendMessage(EmbedUtils.embedImage("https://cdn.discordapp.com/attachments/645864864752861184/657355587947986952/Screenshot_992.png").setColor(event.getMember().getColor()).addField("30 year old Pedophile:", "<@447803380958756864>", false).build()).queue();
//                event.getChannel().sendMessage(EmbedUtils.embedImage( "https://cdn.discordapp.com/attachments/645864864752861184/657355585263632414/Screenshot_990.png").setColor(event.getMember().getColor()).addField("Dated 11 year old named tae:", "<@447803380958756864>", false).build()).queue();
//            }
            if (!event.getMessage().getMentionedMembers().isEmpty() && event.getMessage().getMentionedMembers().get(0).getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
                MessageEmbed embed = new EmbedBuilder().addField("Current prefix:" , Core.PREFIX, false).setColor(Wrapper.getRandomColor()).build();
                event.getChannel().sendMessage(embed).queue();
            }
//            if (msg.contains("comu") || msg.contains("<@175728291460808706>") || msg.contains("august") || msg.contains("auggie") || msg.contains("c o m u")) {
//                event.getChannel().sendMessage("<@175728291460808706> is god").queue();
//            }
//            if (msg.contains("maddie") || msg.contains("lol no") || msg.contains("madie") || msg.contains("<@546554963942440991>") || msg.contains("b.") || msg.contains("b dot")) {
//                event.getChannel().sendMessage("maddie from purple prison?").queue();
//            }
//
//            if (msg.contains("terror") || msg.contains("<@197969769209528320>")) {
//                event.getChannel().sendMessage("<@197969769209528320> got 8 hearted by a 12 year old").queue();
//            }
//
//            if (!(event.getMessage().getAuthor().getId().equals("175728291460808706"))) {
//                if (msg.contains("Ńįggã") || msg.contains("nigga") || msg.contains("nigger") || msg.contains("n3gger") || msg.contains("nihga") || msg.contains("Ñįggæ") || msg.contains("n@gg3r") || msg.contains("n!gga") || msg.contains("nword") || msg.contains("n word") || msg.contains("n-word") || msg.contains("n!gger") || msg.contains("negro") || (!msg.contains("night") && msg.contains("nig")) || msg.contains("nigg") || msg.contains("n i g g a")) {
//                    event.getMessage().delete().queue();
//                    event.getChannel().sendMessage("No racism or toxicity please! 💋").queue();
//                }
//            }
//
//            if (!msg.contains("i love xia") && !msg.contains("i love xia bot") &&!msg.contains("i love you xia") &&  !msg.contains("ily xia") &&  !msg.contains("love you xia bot") &&  !msg.contains("love you xia") && !msg.equalsIgnoreCase("hi xia") && !msg.equalsIgnoreCase("hi xia bot") && !msg.equalsIgnoreCase("hello xia bot") && !msg.equalsIgnoreCase("hello xia")&& msg.contains("xia")) {
//                event.getChannel().sendMessage("You called for me?").queue();
//            }
//            if (!(msg.contains("this")  || msg.contains("watching")|| msg.contains("searching")|| msg.contains("think") || msg.contains("while") || msg.contains("child") || msg.contains("hippo") || msg.contains("hint") || msg.contains("nothing") || msg.contains("thing") || msg.contains("shit")|| msg.contains("kachigga")|| msg.contains("whitelist")) && (msg.contains("hi xia") || msg.contains("hi xia bot") || msg.contains("hello xia") || msg.contains("hello xia bot") || msg.contains("hi") || msg.contains("hey") || msg.contains("hello") || msg.equals("yo") || msg.equals("sup"))) {
//                event.getChannel().sendMessage("Hello!").queue();
//            }
//            if (msg.contains("i love you xia") || msg.contains("ily xia") || msg.contains("love you xia bot") || msg.contains("love you xia") || msg.contains("i love xia") || msg.contains("i love xia bot") || msg.contains("faggot")) {
//                event.getChannel().sendMessage("I love you too!");
//            }

        }
    }
}
