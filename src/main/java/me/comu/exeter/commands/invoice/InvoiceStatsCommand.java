package me.comu.exeter.commands.invoice;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.Invoice;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class InvoiceStatsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You aren't authorized to run this command").build()).queue();
            return;
        }
        // TODO: make the interface look cleaner
        if (!args.isEmpty()) {
            if (args.get(0).equalsIgnoreCase("list")) {
                sendListMessage(event, null);
                return;
            } else if (args.get(0).matches("\\d\\d/\\d\\d/\\d\\d\\d\\d")) {
                List<Invoice> invoiceByDate = Invoice.getInvoiceByDate(args.get(0));
                if (invoiceByDate.isEmpty()) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No invoices found on `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "`.").build()).queue();
                } else {
                    sendListMessage(event, args.get(0));
                }
                return;
            } else if (args.get(0).length() == 8) {
                Invoice invoice = Invoice.getInvoiceByID(args.get(0));
                if (invoice == null) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No invoice exists with ID: `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "`.").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(getInvoiceMessage(invoice).build()).queue();
                }
                return;
            } else if (args.get(0).equalsIgnoreCase("alltime")) {
                String future = "861083085046939689";
                String tiger = "1017869440035074169";

                double total = 0;
                double crypto = 0;
                double cashapp = 0;
                double amazongc = 0;
                double other = 0;
                int hCount = 0;
                int tCount = 0;
                int hTotal = 0;
                int tTotal = 0;
                int totalMsgs = 0;
                int totalDays = 1;
                int quotaDays = 0;
                double hTotalAmount = 0;
                double tTotalAmount = 0;
                Date dateObj = new Date();
                List<String> datesChecked = new ArrayList<>();
                String date = Invoice.dateFormat.format(dateObj);
                if (getTotalForDay(date) >= 50) {
                    quotaDays++;
                }
                for (Invoice invoice : Invoice.invoices) {
                    if (!datesChecked.contains(invoice.getDate()) && !date.equalsIgnoreCase(invoice.getDate())) {
                        datesChecked.add(invoice.getDate());
                        totalDays++;
                        if (getTotalForDay(invoice.getDate()) >= 50) {
                            quotaDays++;
                        }
                    }
                    total += invoice.getAmount();
                    totalMsgs += invoice.getTotalMsgs();
                    hTotal += invoice.getFMsgs();
                    tTotal += invoice.getTMsgs();
                    if (invoice.getFMsgs() > invoice.getTMsgs()) {
                        hCount++;
                        hTotalAmount += invoice.getAmount();
                    } else if (invoice.getFMsgs() < invoice.getTMsgs()) {
                        tCount++;
                        tTotalAmount += invoice.getAmount();
                    } else {
                        if (invoice.getInvoicer().equalsIgnoreCase(future)) {
                            hCount++;
                            hTotalAmount += invoice.getAmount();
                        } else if (invoice.getInvoicer().equalsIgnoreCase(tiger)) {
                            tCount++;
                            tTotalAmount += invoice.getAmount();
                        }
                    }
                    if (invoice.getPaymentGateway().equalsIgnoreCase("cashapp")) {
                        cashapp += invoice.getAmount();
                    } else if (invoice.getPaymentGateway().equalsIgnoreCase("crypto")) {
                        crypto += invoice.getAmount();
                    } else if (invoice.getPaymentGateway().equalsIgnoreCase("amazongc")) {
                        amazongc += invoice.getAmount();
                    } else {
                        other += invoice.getAmount();
                    }
                }
                double avgPerOrder = (double) Math.round(total / Invoice.invoices.size() * 100) / 100;
                double avgPerDay = (double) Math.round(total / totalDays * 100) / 100;
                double avgCashapp = (double) Math.round(cashapp / totalDays * 100) / 100;
                double avgCrypto = (double) Math.round(crypto / totalDays * 100) / 100;
                double avgAmazonGC = (double) Math.round(amazongc / totalDays * 100) / 100;
                double avgOther = (double) Math.round(other / totalDays * 100) / 100;
                int avgTicketsPerDay = Invoice.invoices.size() / totalDays;
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Core.getInstance().getColorTheme());
                embedBuilder.setAuthor("Invoice Statistics");
                embedBuilder.setDescription("**All-Time Records**");
                embedBuilder.addField("Total", "`$" + (total == 0 ? String.format("%.0f", total) : String.format("%.2f", total)) + "`", true);
                embedBuilder.addField("Tickets", "`" + Invoice.invoices.size() + "`", true);
                embedBuilder.addField("CashApp", "`$" + (cashapp == 0 ? String.format("%.0f", cashapp) : String.format("%.2f", cashapp)) + "`\n`$" + (avgCashapp == 0 ? String.format("%.0f", avgCashapp) : String.format("%.2f", avgCashapp))+ "/day`", true);
                embedBuilder.addField("Amazon GC", "`$" + (amazongc == 0 ? String.format("%.0f", amazongc) : String.format("%.2f", amazongc)) + "`\n`$" + (avgAmazonGC == 0 ? String.format("%.0f", avgAmazonGC) : String.format("%.2f", avgAmazonGC)) + "/day`", true);
                embedBuilder.addField("Crypto", "`$" + (crypto == 0 ? String.format("%.0f", crypto) : String.format("%.2f", crypto)) + "`\n`$" + (avgCrypto == 0 ? String.format("%.0f", avgCrypto) : String.format("%.2f", avgCrypto)) + "/day`", true);
                embedBuilder.addField("Other", "`$" + (other == 0 ? String.format("%.0f", other) : String.format("%.2f", other)) + "`\n`$" + (avgOther == 0 ? String.format("%.0f", avgOther) : String.format("%.2f", avgOther)) + "/day`", true);
                embedBuilder.addField("Information", "```Quota Days Reached: " + quotaDays + "/" + totalDays +  "\nTotal Messages: " + totalMsgs + "\nAverage: $" + avgPerOrder + " per order\n\nFuture: \n\t" + hCount + " tickets\n\t" + hCount / totalDays + " tickets/day\n\t" + "$" + String.format("%.2f", hTotalAmount / totalDays) + "/day\n\t$" + String.format("%.2f", hTotalAmount / Invoice.invoices.size()) + "/ticket\n\t" + hTotal / Invoice.invoices.size() + " msgs/ticket\n\t" + hTotal / totalDays + " msgs/day" + "\nTiger: \n\t" + tCount + " tickets\n\t" + tCount / totalDays + " tickets/day\n\t$" + String.format("%.2f", tTotalAmount / totalDays) + "/day\n\t$" + String.format("%.2f", tTotalAmount / Invoice.invoices.size()) + " /ticket\n\t" + tTotal / Invoice.invoices.size() + " msgs/ticket\n\t" + tTotal / totalDays + " msgs/day" + "```", false);
                embedBuilder.setFooter("Average: " + avgTicketsPerDay + " orders/day • " + "$" + (avgPerDay == 0 ? String.format("%.0f", avgPerDay) : String.format("%.2f", avgPerDay) + "/day"));
                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            } else if (args.get(0).equalsIgnoreCase("future")) {

            } else if (args.get(0).equalsIgnoreCase("tiger")) {

            } else {
                sendListMessage(event, null);
                return;
            }
        }

        String future = "861083085046939689";
        String tiger = "1017869440035074169";

        double total = 0;
        double totalToday = 0;
        double yesterdayTotal = 0;
        double crypto = 0;
        double cashapp = 0;
        double amazongc = 0;
        double other = 0;
        int invoicesToday = 0;
        int hCount = 0;
        int tCount = 0;

        Date dateObj = new Date();
        String date = Invoice.dateFormat.format(dateObj);
        String yesterday = Invoice.dateFormat.format(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        for (Invoice invoice : Invoice.invoices) {
            total += invoice.getAmount();
            if (date.equalsIgnoreCase(invoice.getDate())) {
                invoicesToday++;
                if (invoice.getFMsgs() > invoice.getTMsgs()) {
                    hCount++;
                } else if (invoice.getFMsgs() < invoice.getTMsgs()) {
                    tCount++;
                } else {
                    if (invoice.getInvoicer().equalsIgnoreCase(future)) {
                        hCount++;
                    } else if (invoice.getInvoicer().equalsIgnoreCase(tiger)) {
                        tCount++;
                    }
                }
                if (invoice.getPaymentGateway().equalsIgnoreCase("cashapp")) {
                    totalToday += invoice.getAmount();
                    cashapp += invoice.getAmount();
                } else if (invoice.getPaymentGateway().equalsIgnoreCase("crypto")) {
                    crypto += invoice.getAmount();
                    totalToday += invoice.getAmount();
                } else if (invoice.getPaymentGateway().equalsIgnoreCase("amazongc")) {
                    totalToday += invoice.getAmount();
                    amazongc += invoice.getAmount();
                } else {
                    totalToday += invoice.getAmount();
                    other += invoice.getAmount();
                }
            }
            if (invoice.getDate().equalsIgnoreCase(yesterday)) {
                yesterdayTotal += invoice.getAmount();
            }
        }
        double avg = (double) Math.round(total / Invoice.invoices.size() * 100) / 100;
        double avgToday = (double) Math.round(totalToday / (hCount + tCount) * 100) / 100;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Core.getInstance().getColorTheme());
        embedBuilder.setAuthor(date);
        embedBuilder.setDescription("**All-Time:** `$" + (total == 0 ? String.format("%.0f", total) : String.format("%.2f", total)) + " (" + Invoice.invoices.size() + ")`");
        embedBuilder.addField("Total", "`$" + (totalToday == 0 ? String.format("%.0f", totalToday) : String.format("%.2f", totalToday)) + "`", true);
        embedBuilder.addField("Invoices", "`" + invoicesToday + " Today`", true);
        embedBuilder.addField("CashApp", "`$" + (cashapp == 0 ? String.format("%.0f", cashapp) : String.format("%.2f", cashapp)) + "`", true);
        embedBuilder.addField("Amazon GC", "`$" + (amazongc == 0 ? String.format("%.0f", amazongc) : String.format("%.2f", amazongc)) + "`", true);
        embedBuilder.addField("Crypto", "`$" + (crypto == 0 ? String.format("%.0f", crypto) : String.format("%.2f", crypto)) + "`", true);
        embedBuilder.addField("Other", "`$" + (other == 0 ? String.format("%.0f", other) : String.format("%.2f", other)) + "`", true);
        embedBuilder.addField("Today's Info", totalToday >= 50 ? "```Quota Reached \u2705\nAverage: $" + avgToday + "\nFuture: " + hCount + "\nTiger: " + tCount + "```" : "```Quota Reached \u274C\nAverage: $" + avgToday + "\nFuture: " + hCount + "\nTiger: " + tCount + "```", false);
        embedBuilder.setFooter("Total Yesterday: $" + (yesterdayTotal == 0 ? String.format("%.0f", yesterdayTotal) : String.format("%.2f", yesterdayTotal)) + " • " + "Average: " + "$" + (avg == 0 ? String.format("%.0f", avg) : String.format("%.2f", avg) + "/day"));
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    private EmbedBuilder getInvoiceMessage(Invoice invoice) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Core.getInstance().getColorTheme());
        embedBuilder.setTitle("Invoice #" + invoice.getInvoiceNumber());
        embedBuilder.setDescription("*Invoiced by " + Objects.requireNonNull(Core.getInstance().getJDA().getUserById(invoice.getInvoicer())).getAsTag() + "*");
        embedBuilder.addField("Admin", "`" + invoice.getUserTag() + "`", true);
        embedBuilder.addField("Payment Gateway", "`" + invoice.getPaymentGateway() + "`", true);
        embedBuilder.addField("Amount", String.format("`%.02f`", invoice.getAmount()), true);
        embedBuilder.addField("Future", "`" + (int) (invoice.getFMsgs() / (float) invoice.getTotalMsgs() * 100) + "%`", true);
        embedBuilder.addField("Tiger", "`" + (int) ((float) invoice.getTMsgs() / invoice.getTotalMsgs() * 100) + "%`", true);
        embedBuilder.addField("Msg Count", "`" + invoice.getTotalMsgs() + "`", true);
        if (invoice.hasNotes()) embedBuilder.addField("Notes", "```\n" + invoice.getNotes() + "```", true);
        embedBuilder.setFooter("ID: " + invoice.getId() + " • " + invoice.getDate());
        return embedBuilder;
    }

    private void sendListMessage(GuildMessageReceivedEvent event, String date) {
        HashMap<Invoice, String> invoiceDateAmountMap = getInvoiceDateAmountMap(date);
        if (invoiceDateAmountMap.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("There are no invoices logged.").build()).queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        invoiceDateAmountMap.keySet().forEach(invoice -> stringBuilder.append("`").append(invoice.getDate()).append("` - **$").append(String.format("%.02f", invoice.getAmount())).append("** (").append(invoice.getId()).append(")\n"));
        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(date == null ? invoiceDateAmountMap.size() + " Invoices\n" : invoiceDateAmountMap.size() + " Invoices on " + date + " - $" + getTotalForDay(date) + "\n").setColor(Core.getInstance().getColorTheme()).setFooter("Requested by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now());
        ArrayList<Page> pages = new ArrayList<>();
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setContent(stringBuilder.toString());
        Queue<Message> messages = messageBuilder.buildAll(MessageBuilder.SplitPolicy.ANYWHERE);
        for (Message message : messages) {
            embedBuilder.setDescription(message.getContentRaw());
            pages.add(new Page(PageType.EMBED, embedBuilder.build()));
        }
        event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, false, 60, TimeUnit.SECONDS));
    }

    private HashMap<Invoice, String> getInvoiceDateAmountMap(String date) {
        HashMap<Invoice, String> hashMap = new HashMap<>();
        if (date == null) {
            for (Invoice invoice : Invoice.invoices) {
                hashMap.put(invoice, invoice.getId());
            }
        } else {
            for (Invoice invoice : Invoice.invoices) {
                if (invoice.getDate().equalsIgnoreCase(date)) {
                    hashMap.put(invoice, invoice.getId());
                }
            }
        }
        return hashMap;
    }

    private double getTotalForDay(String date) {
        double amount = -1;
        for (Invoice invoice : Invoice.invoices) {
            if (invoice.getDate().equalsIgnoreCase(date)) {
                if (amount == -1) amount = 0;
                amount += invoice.getAmount();
            }
        }
        return amount;
    }


    @Override
    public String getHelp() {
        return "Checks the Invoice stats\n`" + Core.PREFIX + getInvoke() + " [id]/[list]/[date]/[alltime]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "invoicestats";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"istats", "invoicestat", "invoicestatistics", "statisticsinvoices", "statinvoices", "statisticsinvoice", "invoicestats", "invoices", "invoicefetch", "getinvoice", "invoiceget", "seeinvoice", "invoicesee"};
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



