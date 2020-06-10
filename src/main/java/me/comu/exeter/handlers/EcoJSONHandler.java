package me.comu.exeter.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.economy.EconomyManager;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class EcoJSONHandler {

    public static void saveEconomyConfig() {
        JSONObject jsonObject = new JSONObject(EconomyManager.getUsers());
        try (FileWriter fileWriter = new FileWriter("economy.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved economy.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public static void loadEconomyConfig(File file) {
        if (!file.exists())
        {
            boolean didMake = file.mkdir();
            if (didMake)
                Logger.getLogger().print("Created economy.json");
            else
                Logger.getLogger().print("Failed to create economy.json");
        }
        try {
            HashMap<String, Integer> userDoubleHashMap = new ObjectMapper().readValue(file, HashMap.class);
            EconomyManager.setUsers(userDoubleHashMap);
            Logger.getLogger().print("Loaded economy.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
