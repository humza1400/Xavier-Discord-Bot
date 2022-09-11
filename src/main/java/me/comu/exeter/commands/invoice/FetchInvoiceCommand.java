package me.comu.exeter.commands.invoice;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class FetchInvoiceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to fetch invoices.").build()).queue();
            return;
        }
       

    }


    @Override
    public String getHelp() {
        return "Fetches the specified Invoice\n`" + Core.PREFIX + getInvoke() + " [identifier/filter]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "fetchinvoice";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"invoicefetch", "getinvoice", "invoiceget", "seeinvoice","invoicesee"};
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



