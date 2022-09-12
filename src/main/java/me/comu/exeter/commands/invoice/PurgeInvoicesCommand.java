package me.comu.exeter.commands.invoice;


import me.comu.exeter.core.Core;
import me.comu.exeter.handler.handlers.EcoJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.Invoice;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class PurgeInvoicesCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No permission.").build()).queue();
            return;
        }
        if (Invoice.invoices.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There are no invoices to purge.").build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(Utility.embed("Purged `" + Invoice.invoices.size() + "` invoices.").build()).queue();
            Invoice.invoices.clear();
            Core.getInstance().saveConfig(Core.getInstance().getInvoiceHandler());
        }
    }

    @Override
    public String getHelp() {
        return "Purges ALL logged invoices.\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "purgeinvoices";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"deleteinvoices", "deleteallinvoices", "cleaninvoices", "clearinvoices"};
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

