package me.comu.exeter.commands.ticket;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.objects.Ticket;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CloseTicketCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        // TODO: fix

//        if (Objects.equals(Ticket.getAuthorById(compositeKey.getGuildID()), event.getAuthor().getId()) && compositeKey.getUserID().equals(event.getGuild().getId())) {
//            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Looks like you already have a ticket open. Please deal with that one first before opening another one.").queue(succes2s -> succes2s.delete().queueAfter(5, TimeUnit.SECONDS), failure -> System.out.println("[Error] Tried deleting a message from a deleted channel"));
//            return;
//        }
        Ticket ticket = Ticket.getTicketByAuthorAndGuild(event.getAuthor().getId(), event.getGuild().getId());
        if (ticket == null) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(event.getAuthor().getAsMention() + ", you don't have any open tickets!").build()).queue(success2 -> success2.delete().queueAfter(5, TimeUnit.SECONDS));
        } else {
            if (Ticket.isTicketChannel(event.getChannel().getId())) {
                if (!event.getAuthor().getId().equals(ticket.getAuthor()) || !Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER)) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't close a ticket that doesn't belong to you!").build()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(Utility.embed("\u2699 Attempting to close your ticket...").build()).queue();
                ticket.setTimeClosed(Instant.now());
                ticket.setCloser(event.getAuthor().getId());
                StringBuilder transcript = new StringBuilder();
                event.getChannel().getIterableHistory().takeAsync(10000).thenApply(list -> {
                    list.forEach(msg -> transcript.append(msg.getTimeCreated().format(Utility.dtf)).append(" ").append(msg.getAuthor().getAsTag()).append(" : ").append(msg.getContentRaw()).append("\n"));
                    ticket.setTranscript(transcript.toString());
                    Objects.requireNonNull(Objects.requireNonNull(event.getGuild().getCategoryById(SetupTicketCommand.ticketCategory.get(event.getGuild().getId()))).getPermissionOverride(Objects.requireNonNull(event.getGuild().getMemberById(ticket.getAuthor())))).delete().queue();
                    Objects.requireNonNull(event.getGuild().getTextChannelById(ticket.getChannel())).delete().queue();
                    SetupTicketCommand.openGuildTickets.remove(WhitelistKey.of(ticket.getId(), event.getGuild().getId()));
                    Core.getInstance().saveConfig(Core.getInstance().getTicketHandler());
                    return list;
                });
            } else {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can only close tickets in your ticket-channel. Navigate to your ticket and re-close it there.").build()).queue();
            }
        }


    }


    @Override
    public String getHelp() {
        return "Closes the ticket\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "closeticket";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"close", "ticketclose"};
    }

    @Override
    public ICommand.Category getCategory() {
        return Category.TICKET;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}



