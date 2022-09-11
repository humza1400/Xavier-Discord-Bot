package me.comu.exeter.events;

import me.comu.exeter.commands.ticket.SetupTicketCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.objects.Ticket;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketEvent extends ListenerAdapter {

    @Override
    public void onButtonClick(@Nonnull ButtonClickEvent event) {
        if (event.getGuild() != null && SetupTicketCommand.ticketMessage.containsKey(event.getGuild().getId()) && event.getComponentId().equals(SetupTicketCommand.ticketMessage.get(event.getGuild().getId()))) {
            User user = event.getUser();
            Category category = event.getGuild().getCategoryById(SetupTicketCommand.ticketCategory.get(event.getGuild().getId()));
            if (category == null) {
                event.reply("Uh oh, looks like the ticket-category was deleted, contact an administrator for further assistance").queue(msg -> msg.deleteOriginal().queueAfter(9, TimeUnit.SECONDS));
                return;
            }
            if (!SetupTicketCommand.openGuildTickets.isEmpty()) {
                for (WhitelistKey whitelistKey : SetupTicketCommand.openGuildTickets.keySet()) {
                    if (Objects.equals(Ticket.getAuthorById(whitelistKey.getGuildID()), user.getId()) && whitelistKey.getUserID().equals(event.getGuild().getId())) {
                        event.reply(event.getUser().getAsMention() + ", Looks like you already have a ticket open. Please deal with that one first before opening another one.").queue(succes2s -> succes2s.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
                        return;
                    }
                }
            }
            event.getGuild().createTextChannel(user.getName()).queue(success -> {


                String hash = Utility.generateRandomString(5);
                success.getManager().setParent(category).queue(manager -> success.getManager().putPermissionOverride(Objects.requireNonNull(event.getGuild().getMember(user)), Collections.singletonList(Permission.VIEW_CHANNEL), null).putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singletonList(Permission.VIEW_CHANNEL)).queue(
                        permission -> category.upsertPermissionOverride(Objects.requireNonNull(event.getGuild().getMember(user))).setAllow(Permission.VIEW_CHANNEL).queue(caegoryPermission -> {
                            event.reply(user.getAsMention() + " Created your ticket! ").queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS), failure -> event.getChannel().sendMessage("Couldn't create ticket! Please contact an administrator for further assistance.").queue());
                            List<String> members = new ArrayList<>();
                            success.getMembers().stream().filter(member -> !member.getUser().isBot()).forEach(member -> members.add(member.getId()));
                            Ticket ticket = Ticket.of(user.getId(), Objects.requireNonNull(event.getGuild()).getId(), success.getId(), hash, members, Instant.now());
                            Ticket.tickets.add(ticket);
                            SetupTicketCommand.openGuildTickets.put(WhitelistKey.of(ticket.getId(), event.getGuild().getId()), ticket);
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(Color.RED);
                            embedBuilder.setTitle("Welcome to your ticket!");
                            embedBuilder.setTimestamp(Instant.now());
                            embedBuilder.setDescription("Please be patient and an administrator will be with you\n\nTo close your ticket, type: `" + Core.PREFIX + "close`");
                            embedBuilder.setFooter("ID: " + hash);
                            success.sendMessageEmbeds(embedBuilder.build()).queue();
                        })));
            });
        }
    }
}
