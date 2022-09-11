package me.comu.exeter.commands.invoice;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.Invoice;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.*;


public class InvoiceCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to Invoice any orders.").build()).queue();
            return;
        }
        if (args.size() < 2) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Missing invoice arguments, `" + Core.PREFIX + "help " + getInvoke() + "`").build()).queue();
            return;
        }
//        if (Invoice.invoiceChannel == null)
//        {
//            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No invoice channel has been set yet.").build()).queue();
//            return;
//        }
//        TextChannel textChannel = event.getJDA().getTextChannelById(Invoice.invoiceChannel);
//        if (textChannel == null)
//        {
//            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("The previous invoices channel appears to have been deleted or I no longer have access to it, please setup a new one.").build()).queue();
//            return;
//        }
        String future = "861083085046939689";
        String tiger = "1017869440035074169";
        boolean mentionedMember = event.getMessage().getMentionedMembers().size() > 0;
        Member member = event.getMember();
        if (mentionedMember) member = event.getMessage().getMentionedMembers().get(0);
        String user = "`" + member.getUser().getAsTag() + "`";
        String id = Utility.generateRandomString(8);
        String date = Invoice.dateFormat.format(new Date());
        String paymentGateway;
        String amount;
        Invoice invoice;
        int[] fMsgCount = {0};
        int[] tMsgCount = {0};
        int[] totalMsgCount = {0};
        boolean hasNote = args.size() > 3;
        try {
            amount = "`$" + (mentionedMember ? Double.parseDouble(args.get(1)) : Double.parseDouble(args.get(0))) + "`";
        } catch (NumberFormatException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid amount.").build()).queue();
            return;
        }

        event.getChannel().getIterableHistory().forEach(msg -> {
            totalMsgCount[0]++;
           if (msg.getAuthor().getId().equals(future))
               fMsgCount[0]++;
           if (msg.getAuthor().getId().equals(tiger))
               tMsgCount[0]++;
        });
        if (fMsgCount[0] > tMsgCount[0]) {
            user = Objects.requireNonNull(event.getGuild().getMemberById(future)).getUser().getAsTag();
        } else if (fMsgCount[0] < tMsgCount[0]){
            user = Objects.requireNonNull(event.getGuild().getMemberById(tiger)).getUser().getAsTag();
        }
        String type = mentionedMember ? args.get(2) : args.get(1);
        Color color;
        if (type.equalsIgnoreCase("cash") || type.equalsIgnoreCase("cashapp")) {
            paymentGateway = "CashApp";
            color = Color.GREEN;
        } else if (type.equalsIgnoreCase("crypto")) {
            paymentGateway = "Crypto";
            color = Color.yellow;
        } else if (type.equalsIgnoreCase("amazon") || type.equalsIgnoreCase("amazongc") || type.equalsIgnoreCase("amazongiftcard") || type.equalsIgnoreCase("gc")) {
            color = Color.blue;
            paymentGateway = "AmazonGC";
        } else {
            color = Color.GRAY;
            paymentGateway = type;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (hasNote) {
            if (mentionedMember) {
                args.stream().skip(3).forEach(stringJoiner::add);
            } else {
                args.stream().skip(2).forEach(stringJoiner::add);
            }
            invoice = Invoice.of(event.getAuthor().getId(), Invoice.invoices.size() + 1, member.getUser().getAsTag(), id, mentionedMember ? Double.parseDouble(args.get(1)) : Double.parseDouble(args.get(0)), paymentGateway, stringJoiner.toString(), date, fMsgCount[0], tMsgCount[0], totalMsgCount[0]);
        } else {
            invoice = Invoice.of(event.getAuthor().getId(), Invoice.invoices.size() + 1, member.getUser().getAsTag(), id, mentionedMember ? Double.parseDouble(args.get(1)) : Double.parseDouble(args.get(0)), paymentGateway, date, fMsgCount[0], tMsgCount[0], totalMsgCount[0]);
        }
        Invoice.addInvoice(invoice);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(color);
        embedBuilder.setTitle("Invoice #" + invoice.getInvoiceNumber());
        embedBuilder.setDescription("*Invoiced by " + event.getAuthor().getAsTag() + "*");
        embedBuilder.addField("Admin", "`" + user + "`", true);
        embedBuilder.addField("Payment Gateway", "`" + paymentGateway + "`", true);
        embedBuilder.addField("Amount", amount, true);
        embedBuilder.addField("Future", "`" + (int)(fMsgCount[0]/(float)totalMsgCount[0]*100) + "%`", true);
        embedBuilder.addField("Tiger", "`" + (int)((float)tMsgCount[0]/totalMsgCount[0]*100) + "%`", true);
        embedBuilder.addField("Msg Count", "`" + totalMsgCount[0] + "`", true);
        if (hasNote) embedBuilder.addField("Notes", "```\n" + stringJoiner + "```", true);
        embedBuilder.setFooter("ID: " + id + " • " + date);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        Core.getInstance().saveConfig(Core.getInstance().getInvoiceHandler());
    }

    @Override
    public String getHelp() {
        return "Creates an Invoice\n`" + Core.PREFIX + getInvoke() + " [user] [amount] [payment-gateway] [notes]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "invoice";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"addorder", "order", "addinvoice", "invoiceadd", "orderadd"};
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


