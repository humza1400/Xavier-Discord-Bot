package me.comu.exeter.commands.ticket;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.objects.Ticket;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CreateTicketCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (SetupTicketCommand.ticketCategory.containsKey(event.getGuild().getId())) {
            net.dv8tion.jda.api.entities.Category category = event.getGuild().getCategoryById(SetupTicketCommand.ticketCategory.get(event.getGuild().getId()));
            if (category == null) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Uh oh, looks like the ticket-category was deleted, contact an administrator for further assistance.").build()).queue();
                return;
            }
            if (!SetupTicketCommand.openGuildTickets.isEmpty()) {
                for (WhitelistKey whitelistKey : SetupTicketCommand.openGuildTickets.keySet()) {
                    if (Ticket.getAuthorById(whitelistKey.getGuildID()) == null)
                    {
                        event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Well this is awkward, something went wrong and I don't know what.").build()).queue();
                        return;
                    }
                    if (Objects.equals(Ticket.getAuthorById(whitelistKey.getGuildID()), event.getAuthor().getId()) && whitelistKey.getUserID().equals(event.getGuild().getId())) {
                        event.getChannel().sendMessageEmbeds(Utility.embed(event.getAuthor().getAsMention() + ", Looks like you already have a ticket open. Please deal with that one first before opening another one.").build()).queue(succes2s -> succes2s.delete().queueAfter(5, TimeUnit.SECONDS), failure -> System.out.println("[Error] Tried deleting a message from a deleted channel"));
                        return;
                    }
                }
            }
            event.getGuild().createTextChannel(event.getAuthor().getName()).queue(success -> {
                String hash = Utility.generateRandomString(5);
                success.getManager().setParent(category).queue(parent -> success.getManager().putPermissionOverride(Objects.requireNonNull(event.getMember()), Collections.singletonList(Permission.VIEW_CHANNEL), null).putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singletonList(Permission.VIEW_CHANNEL)).queue(channelPermission -> category.upsertPermissionOverride(event.getMember()).setAllow(Permission.VIEW_CHANNEL).queue(success3 -> {
                    event.getChannel().sendMessageEmbeds(Utility.embed(event.getAuthor().getAsMention() + " Created your ticket!").build()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS), failure -> event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't create ticket! Please contact an administrator for further assistance.").build()).queue());
                    List<String> members = new ArrayList<>();
                    success.getMembers().stream().filter(member -> !member.getUser().isBot()).forEach(member -> members.add(member.getId()));
                    Ticket ticket = Ticket.of(event.getAuthor().getId(), Objects.requireNonNull(event.getGuild()).getId(), success.getId(), hash, members, Instant.now());
                    Ticket.tickets.add(ticket);
                    SetupTicketCommand.openGuildTickets.put(WhitelistKey.of(ticket.getId(), event.getGuild().getId()), ticket);
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(Core.getInstance().getColorTheme());
                    embedBuilder.setTitle("Welcome to your ticket!");
                    embedBuilder.setTimestamp(Instant.now());
                    embedBuilder.setDescription("Please be patient and an administrator will be with you\n\nTo close your ticket, type: `" + Core.PREFIX + "close`");
                    embedBuilder.setFooter("ID: " + hash);
                    success.sendMessageEmbeds(embedBuilder.build()).queue();
                })));
            });

        } else {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Looks like the server hasn't set up tickets yet, please contact an administrator for more help.").build()).queue();
        }

    }


    @Override
    public String getHelp() {
        return "Creates a ticket if the Ticket System is set up\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "createticket";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"create", "ticketcreate"};
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



