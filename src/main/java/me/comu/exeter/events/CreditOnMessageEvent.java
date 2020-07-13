package me.comu.exeter.events;

import me.comu.exeter.commands.bot.ShowCreditMessagesCommand;
import me.comu.exeter.commands.economy.EconomyManager;
import me.comu.exeter.commands.misc.AFKCommand;
import me.comu.exeter.util.ChatTrackingManager;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CreditOnMessageEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        double d = Math.random();
        int amount = new Random().nextInt(31);
        if (d > 0.98 && amount != 0) {
            if (ShowCreditMessagesCommand.creditNotifications && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
                event.getChannel().sendMessage(Utility.removeMarkdown(Objects.requireNonNull(event.getMember()).getUser().getName()) + " got lucky and received " + MarkdownUtil.monospace(Integer.toString(amount)) + " credits!").queue();

            if (EconomyManager.verifyUser(event.getAuthor().getId()))
                EconomyManager.getUsers().put(event.getAuthor().getId(), 0);
            EconomyManager.setBalance(event.getAuthor().getId(), EconomyManager.getBalance(event.getAuthor().getId()) + amount);
        }

        if (ChatTrackingManager.verifyChatUser(Objects.requireNonNull(event.getMember()).getId())) {
            ChatTrackingManager.setChatCredits(event.getMember().getId(), 0);
        }
        ChatTrackingManager.setChatCredits(event.getMember().getId(), ChatTrackingManager.getChatCredits(event.getMember().getId()) + 1);

        // AFKCommand Event
        if (AFKCommand.afkUsers.containsKey(event.getAuthor().getId())) {
            if (AFKCommand.afkUserMessageIndex.get(event.getAuthor().getId()) == 3) {
                event.getChannel().sendMessage(event.getMember().getAsMention() + " is no longer AFK.").queue();
                AFKCommand.afkUsers.remove(event.getMember().getId());
                AFKCommand.afkUserMessageIndex.remove(event.getMember().getId());
            } else {
                AFKCommand.afkUserMessageIndex.replace(event.getMember().getId(), AFKCommand.afkUserMessageIndex.get(event.getMember().getId()) + 1);
            }
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        memberList.forEach(member -> {
            if (AFKCommand.afkUsers.containsKey(member.getId())) {
                if (AFKCommand.afkUsers.get(member.getId()) == null) {
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getGuild().getMemberById(member.getId())).getUser().getAsTag() + " is currently AFK.").queue();
                } else {
                    String message = Utility.removeMarkdown(AFKCommand.afkUsers.get(member.getId()));
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getGuild().getMemberById(member.getId())).getUser().getAsTag() + " is currently AFK for `" + message + "`").queue();
                }
            }
        });

    }
}
