package me.comu.exeter.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private String invoicer;
    private String userTag;
    private String id;
    private int invoiceNumber;
    private Double amount;
    private String paymentGateway;
    private String notes;
    private String date;
    private int fMsgs;
    private int tMsgs;
    private int totalMsgs;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    public static List<Invoice> invoices = new ArrayList<>();
    public static String invoiceChannel = null;

    public static Invoice of(String invoicer, int invoiceNumber, String userTag, String id, Double amount, String paymentGateway, String date, int fMsgs, int tMsgs, int totalMsgs) {
        return new Invoice(invoicer, invoiceNumber, userTag, id, amount, paymentGateway, date, fMsgs, tMsgs, totalMsgs);
    }

    public static Invoice of(String invoicer, int invoiceNumber, String userTag, String id, Double amount, String paymentGateway, String notes, String date, int fMsgs, int tMsgs, int totalMsgs) {
        return new Invoice(invoicer, invoiceNumber, userTag, id, amount, paymentGateway, notes, date, fMsgs, tMsgs, totalMsgs);
    }

    public Invoice(String invoicer, int invoiceNumber, String userTag, String id, Double amount, String paymentGateway, String date, int fMsgs, int tMsgs, int totalMsgs) {
        this.invoicer = invoicer;
        this.invoiceNumber = invoiceNumber;
        this.userTag = userTag;
        this.id = id;
        this.amount = amount;
        this.paymentGateway = paymentGateway;
        this.notes = null;
        this.date = date;
        this.fMsgs = fMsgs;
        this.tMsgs = tMsgs;
        this.totalMsgs = totalMsgs;
    }

    public Invoice(String invoicer, int invoiceNumber, String userTag, String id, Double amount, String paymentGateway, String notes, String date, int fMsgs, int tMsgs, int totalMsgs) {
        this.invoicer = invoicer;
        this.invoiceNumber = invoiceNumber;
        this.userTag = userTag;
        this.id = id;
        this.amount = amount;
        this.paymentGateway = paymentGateway;
        this.notes = notes;
        this.date = date;
        this.fMsgs = fMsgs;
        this.tMsgs = tMsgs;
        this.totalMsgs = totalMsgs;
    }

    public static void addInvoice(Invoice of) {
        invoices.add(of);
    }

    public String getInvoicer() {
        return invoicer;
    }

    public void setInvoicer(String invoicer) {
        this.invoicer = invoicer;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static void loadInvoices(List<Invoice> list) {
        invoices = list;
    }

    public int getFMsgs() {
        return fMsgs;
    }

    public void setFMsgs(int fMsgs) {
        this.fMsgs = fMsgs;
    }

    public int getTMsgs() {
        return tMsgs;
    }

    public void setTMsgs(int tMsgs) {
        this.tMsgs = tMsgs;
    }

    public int getTotalMsgs() {
        return totalMsgs;
    }

    public boolean hasNotes() {
        return notes != null;
    }

    public void setTotalMsgs(int totalMsgs) {
        this.totalMsgs = totalMsgs;
    }

    public static Invoice getInvoiceByID(String id) {
        return Invoice.invoices.stream().filter(invoice -> invoice.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static List<Invoice> getInvoiceByDate(String date) {
        List<Invoice> invoices = new ArrayList<>();
        Invoice.invoices.stream().filter(invoice -> invoice.getDate().equalsIgnoreCase(date)).forEach(invoices::add);
        return invoices;
    }
}
