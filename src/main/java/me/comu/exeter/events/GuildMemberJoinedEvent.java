package me.comu.exeter.events;

import me.comu.exeter.commands.AntiRaidCommand;
import me.comu.exeter.commands.SetWelcomeChannelCommand;
import me.comu.exeter.commands.ToggleWelcomeCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

public class GuildMemberJoinedEvent extends ListenerAdapter {
    private static final String[] messages = {"[member] joined","welcome [member]", "hello [member]","[member] has arrived", "we've been missing you [member]", "[member] is amazing for joining","Yo [member]","everyone welcome [member]","what it do [member]"};

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    
        if (AntiRaidCommand.isActive()) {
            if (event.getMember().getUser().isBot()) {
                
                Member member = event.getMember();
                event.getGuild().ban(member, 1).reason("wizz??").queue();
                User userComu = event.getJDA().getUserById("175728291460808706");
                User userDamon = event.getJDA().getUserById("464114153616048131");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now)); //2016/11/16 12:08:43
                sendPrivateMessage(userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`");
                sendPrivateMessage(userDamon, "**Anti-Raid Report**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`");
            }
        }

        if (ToggleWelcomeCommand.isActive()) {
            Random random = new Random();
            int number = random.nextInt(messages.length);
            EmbedBuilder joinEmbed = new EmbedBuilder();
            joinEmbed.setColor(0x66d8ff);
            if (event.getMember().getUser().isBot())
                joinEmbed.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()) + " (BOT)");
                else
            joinEmbed.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));
            if (!SetWelcomeChannelCommand.bound)
            event.getGuild().getDefaultChannel().sendMessage(joinEmbed.build()).queue();
            else
                event.getGuild().getTextChannelById(SetWelcomeChannelCommand.logChannelID).sendMessage(joinEmbed.build()).queue();
            try {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("member", true).get(0)).complete();
            } catch (IndexOutOfBoundsException | HierarchyException ex) {
                event.getGuild().createRole().setName("member").setMentionable(false).setColor(0xfcba03).complete();
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("member", true).get(0)).complete();
            }
        }

    }
    public void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();
        });
    }

}
