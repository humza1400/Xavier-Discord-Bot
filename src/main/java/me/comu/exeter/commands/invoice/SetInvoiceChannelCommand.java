package me.comu.exeter.commands.invoice;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.Invoice;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Deprecated
public class SetInvoiceChannelCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to set the invoice channel.").build()).queue();
            return;
        }
        Invoice.invoiceChannel = event.getChannel().getId();
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully set the Invoice channel to `#" + event.getChannel().getName() + "`").build()).queue();
    }


    @Override
    public String getHelp() {
        return "Sets the Invoice Channel\n`" + Core.PREFIX + getInvoke() + " [channel-id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "setinvoicechannel";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setinvoice", "setinvoiceschannel", "setinvoices", "invoiceset","invoicesset"};
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



