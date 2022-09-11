package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.owner.CommandBlacklistCommand;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CmdBlacklistJSONHandler extends Handler {

    public CmdBlacklistJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        JSONArray jsonObject = new JSONArray(CommandBlacklistCommand.getCommandBlacklist());
        try (FileWriter fileWriter = new FileWriter("cmdblacklist.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved cmdblacklist.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created cmdblacklist.json");
                else
                    Logger.getLogger().print("Failed to create cmdblacklist.json");
            }
            List<String> userDoubleHashMap = new ObjectMapper().readValue(file, ArrayList.class);
            CommandBlacklistCommand.setCommandBlacklist(userDoubleHashMap);
            Logger.getLogger().print("Loaded blacklisted.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
