package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.objects.WhitelistKey;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WhitelistedJSONHandler extends Handler {

    private final Map<String, Map<String, String>> whitelistedIDs = new HashMap<>();

    public WhitelistedJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        for (Map.Entry<WhitelistKey, String> entry : WhitelistCommand.getWhitelistedIDs().entrySet()) {
            WhitelistKey whitelistKey = entry.getKey();
            String permissionLevel = entry.getValue();
            if (!whitelistedIDs.containsKey(whitelistKey.getGuildID()))
                whitelistedIDs.put(whitelistKey.getGuildID(), new HashMap<>());
            whitelistedIDs.get(whitelistKey.getGuildID()).put(whitelistKey.getUserID(), permissionLevel);
        }

        JSONObject jsonObject = new JSONObject(whitelistedIDs);
        try (FileWriter fileWriter = new FileWriter("whitelisted.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved whitelisted.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created whitelisted.json");
                else
                    Logger.getLogger().print("Failed to create whitelisted.json");
            }
            Map<String, Map<String, String>> whitelistedIDs = new ObjectMapper().readValue(file, HashMap.class);
            WhitelistCommand.setWhitelistedIDs(whitelistedIDs);
            Logger.getLogger().print("Loaded whitelisted.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, Map<String, String>> getWhitelistedIDs() {
        return whitelistedIDs;
    }
}
