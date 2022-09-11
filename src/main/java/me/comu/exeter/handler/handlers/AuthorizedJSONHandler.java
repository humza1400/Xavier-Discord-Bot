package me.comu.exeter.handler.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.owner.AuthorizeCommand;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.logging.Logger;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AuthorizedJSONHandler extends Handler {

    public AuthorizedJSONHandler(File file) {
        super(file);
    }

    public void saveConfig() {
        JSONArray jsonObject = new JSONArray(AuthorizeCommand.getAuthorized());
        try (FileWriter fileWriter = new FileWriter("authorized.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved authorized.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfig(File file) {
        try {
            if (!file.exists()) {
                if (file.createNewFile())
                    Logger.getLogger().print("Created authorized.json");
                else
                    Logger.getLogger().print("Failed to create authorized.json");
            }
            List<String> userDoubleHashMap = new ObjectMapper().readValue(file, ArrayList.class);
            AuthorizeCommand.setAuthorized(userDoubleHashMap);
            Logger.getLogger().print("Loaded authorized.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
