package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.objects.Invoice;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvoiceJSONHandler extends Handler {

    public InvoiceJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        try (FileWriter fileWriter = new FileWriter("invoices.json")) {
            JSONArray jsonObject = new JSONArray(Invoice.invoices);
            fileWriter.write(jsonObject.toString());
            Logger.getLogger().print("Saved invoices.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created invoices.json");
                else
                    Logger.getLogger().print("Failed to create invoices.json");
            }
            List<HashMap<String, Object>> invoices = new ObjectMapper().readValue(file, ArrayList.class);
            List<Invoice> patchedInvoices = new ArrayList<>();
            for (HashMap<String, Object> invoice : invoices) {
                if (invoice.get("amount") instanceof Integer) {
                    if (invoice.containsKey("notes")) {
                        patchedInvoices.add(new Invoice(((String) invoice.get("invoicer")), (int) invoice.get("invoiceNumber"), (String) invoice.get("userTag"), (String) invoice.get("id"), (double) (int) invoice.get("amount"), (String) invoice.get("paymentGateway"), (String) invoice.get("notes"), (String) invoice.get("date"), (int) invoice.get("FMsgs"), (int) invoice.get("TMsgs"), (int) invoice.get("totalMsgs")));
                    } else {
                        patchedInvoices.add(new Invoice(((String) invoice.get("invoicer")), (int) invoice.get("invoiceNumber"), (String) invoice.get("userTag"), (String) invoice.get("id"), (double) (int) invoice.get("amount"), (String) invoice.get("paymentGateway"), (String) invoice.get("date"), (int) invoice.get("FMsgs"), (int) invoice.get("TMsgs"), (int) invoice.get("totalMsgs")));
                    }
                } else {
                    if (invoice.containsKey("notes")) {
                        patchedInvoices.add(new Invoice(((String) invoice.get("invoicer")), (int) invoice.get("invoiceNumber"), (String) invoice.get("userTag"), (String) invoice.get("id"), (double) invoice.get("amount"), (String) invoice.get("paymentGateway"), (String) invoice.get("notes"), (String) invoice.get("date"), (int) invoice.get("FMsgs"), (int) invoice.get("TMsgs"), (int) invoice.get("totalMsgs")));
                    } else {
                        patchedInvoices.add(new Invoice(((String) invoice.get("invoicer")), (int) invoice.get("invoiceNumber"), (String) invoice.get("userTag"), (String) invoice.get("id"), (double) invoice.get("amount"), (String) invoice.get("paymentGateway"), (String) invoice.get("date"), (int) invoice.get("FMsgs"), (int) invoice.get("TMsgs"), (int) invoice.get("totalMsgs")));
                    }
                }
            }
            Invoice.loadInvoices(patchedInvoices);
            Logger.getLogger().print("Loaded invoices.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
