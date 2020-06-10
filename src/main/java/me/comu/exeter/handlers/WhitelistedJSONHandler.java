package me.comu.exeter.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.util.CompositeKey;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WhitelistedJSONHandler {

   public static Map<String, Map<String, String>> whitelistedIDs = new HashMap<>();

    public static void saveWhitelistConfig() {
        for (Map.Entry entry : WhitelistCommand.getWhitelistedIDs().entrySet()) {
            CompositeKey compositeKey = (CompositeKey) entry.getKey();
            String permissionLevel = (String) entry.getValue();
            if (!whitelistedIDs.containsKey(compositeKey.getGuildID()))
                whitelistedIDs.put(compositeKey.getGuildID(), new HashMap<>());
            whitelistedIDs.get(compositeKey.getGuildID()).put(compositeKey.getUserID(), permissionLevel);
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
    public static void loadWhitelistConfig(File file) {
        if (!file.exists())
        {
            boolean didMake = file.mkdir();
            if (didMake)
                Logger.getLogger().print("Created whitelisted.json");
            else
                Logger.getLogger().print("Failed to create whitelisted.json");
        }
        try {
            Map<String, Map<String, String>> whitelistedIDs = new ObjectMapper().readValue(file, HashMap.class);
            WhitelistCommand.setWhitelistedIDs(whitelistedIDs);
            Logger.getLogger().print("Loaded whitelisted.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
