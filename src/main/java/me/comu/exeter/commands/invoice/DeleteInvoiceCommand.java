package me.comu.exeter.commands.invoice;


import me.comu.exeter.core.Core;
import me.comu.exeter.handler.handlers.EcoJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.Invoice;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class DeleteInvoiceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(getHelp()).build()).queue();
            return;
        }

        if (Invoice.invoices.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There are no invoices.").build()).queue();
        } else {
            if (args.get(0).matches("\\d\\d/\\d\\d/\\d\\d\\d\\d")) {
                List<Invoice> invoiceByDate = Invoice.getInvoiceByDate(args.get(0));
                if (invoiceByDate.isEmpty()) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There are no logged invoices on `" + args.get(0) + "`.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embed("Deleted `" + invoiceByDate.size() + "` invoices on **" + args.get(0) + "**.").build()).queue();
                    Invoice.invoices.removeAll(invoiceByDate);
                }
            } else {
                Invoice invoice = Invoice.getInvoiceByID(args.get(0));
                if (invoice == null) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There are is no invoice with id `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "`.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.embed("Deleted `" + invoice.getId() + "`.").build()).queue();
                    Invoice.invoices.remove(invoice);
                }
            }


            Core.getInstance().saveConfig(Core.getInstance().getInvoiceHandler());
        }
    }


    @Override
    public String getHelp() {
        return "Deletes specified invoices by either ID or date.\n**Note** Deleting by date will delete ALL invoices on that date\n\n`" + Core.PREFIX + getInvoke() + " <id>/<date>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "deleteinvoice";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"delinvoice", "invoicedelete", "invoicedel", "reminvoice","invoicerem","removeinvoice","invoiceremove"};
    }

    @Override
    public Category getCategory() {
        return Category.TICKET;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}

