package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.objects.Ticket;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketJSONHandler extends Handler {

    public TicketJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        try (FileWriter fileWriter = new FileWriter("tickets.json")) {
            JSONArray jsonObject = new JSONArray(Ticket.tickets);
            fileWriter.write(jsonObject.toString());
            Logger.getLogger().print("Saved tickets.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created tickets.json");
                else
                    Logger.getLogger().print("Failed to create tickets.json");
            }
            List<HashMap<String, Object>> tickets = new ObjectMapper().readValue(file, ArrayList.class);
            List<Ticket> patchedTickets = new ArrayList<>();
            for (HashMap<String, Object> ticket : tickets) {
                patchedTickets.add(new Ticket(((String) ticket.get("author")), (String) ticket.get("guild"), (String) ticket.get("channel"), (String) ticket.get("id"), (String) ticket.get("transcript"), (ArrayList<String>) ticket.get("members"), (String) ticket.get("timeCreated"), (String) ticket.get("timeClosed"), (String) ticket.get("closer")));
            }
            Ticket.loadTickets(patchedTickets);
            Logger.getLogger().print("Loaded tickets.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
