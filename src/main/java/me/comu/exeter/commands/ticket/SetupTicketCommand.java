package me.comu.exeter.commands.ticket;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.objects.Ticket;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.*;


public class SetupTicketCommand implements ICommand {

    public static HashMap<String, String> ticketCategory = new HashMap<>();
    public static HashMap<String, String> ticketMessage = new HashMap<>();
    public static final Map<WhitelistKey, Ticket> openGuildTickets = Collections.checkedMap(new HashMap<>(), WhitelistKey.class, Ticket.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set up the ticket system.").build()).queue();
            return;
        }
        event.getMessage().delete().queue();
        ticketCategory.remove(event.getGuild().getId());
        ticketMessage.remove(event.getGuild().getId());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(event.getGuild().getName());
        embed.setDescription("To create a ticket, React with the \"Create Ticket\" button below.");
        embed.setFooter("Powered by comp Bot", "https://cdn.discordapp.com/attachments/723250694118965300/872365365131370537/5-gzDvuA.png");
        embed.setColor(0x4ce67a);
        String ticketHash = Utility.generateRandomString(7);
        event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(Button.primary(ticketHash, "\u2709 Create Ticket")).queue();
        ticketMessage.put(event.getGuild().getId(), ticketHash);
        event.getGuild().createCategory("Tickets").queue(success -> {
            ticketCategory.put(event.getGuild().getId(), success.getId());
            success.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singletonList(Permission.VIEW_CHANNEL)).queue();
        }, failure -> event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permission to create a ticket-managing instance!").build()).queue());

    }


    @Override
    public String getHelp() {
        return "Creates a Ticket Manager Instance\n`" + Core.PREFIX + getInvoke() + " `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setuptickets";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"setupticket","ticketsetup","setuptick","ticksetup"};
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



