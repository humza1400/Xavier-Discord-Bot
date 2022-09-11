package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.admin.BlacklistCommand;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class BlacklistedJSONHandler extends Handler {

    public BlacklistedJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        JSONObject jsonObject = new JSONObject(BlacklistCommand.getBlacklistedUsers());
        try (FileWriter fileWriter = new FileWriter("blacklisted.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved blacklisted.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created blacklisted.json");
                else
                    Logger.getLogger().print("Failed to create blacklisted.json");
            }
            HashMap<String, String> userDoubleHashMap = new ObjectMapper().readValue(file, HashMap.class);
            BlacklistCommand.setBlacklistedUsers(userDoubleHashMap);
            Logger.getLogger().print("Loaded blacklisted.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
