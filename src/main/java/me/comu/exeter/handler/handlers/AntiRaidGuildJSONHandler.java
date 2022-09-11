package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AntiRaidGuildJSONHandler extends Handler {

    public AntiRaidGuildJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        JSONObject jsonObject = new JSONObject(WhitelistCommand.getGuilds());
        try (FileWriter fileWriter = new FileWriter("antiraid.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved antiraid.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created antiraid.json");
                else
                    Logger.getLogger().print("Failed to create antiraid.json");
            }
            HashMap<String, Boolean> userDoubleHashMap = new ObjectMapper().readValue(file, HashMap.class);
            WhitelistCommand.setGuilds(userDoubleHashMap);
            Logger.getLogger().print("Loaded antiraid.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
