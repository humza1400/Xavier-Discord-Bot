package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.economy.EconomyManager;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class EcoJSONHandler extends Handler {

    public EcoJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
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
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created economy.json");
                else
                    Logger.getLogger().print("Failed to create economy.json");
            }
            HashMap<String, Integer> userDoubleHashMap = new ObjectMapper().readValue(file, HashMap.class);
            EconomyManager.setUsers(userDoubleHashMap);
            Logger.getLogger().print("Loaded economy.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
