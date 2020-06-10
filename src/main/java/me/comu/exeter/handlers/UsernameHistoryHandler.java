package me.comu.exeter.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.commands.bot.UsernameHistoryCommand;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsernameHistoryHandler {

    public static Map<String, Map<String, String>> usernames = new HashMap<>();

    public static void saveUsernameHistoryConfig() {
        for (Map.Entry<String, HashMap<String, String>> entry : UsernameHistoryCommand.usernames.entrySet()) {
            String userId = entry.getKey();
            HashMap<String, String> hashmap = entry.getValue();
//            if (!usernames.containsKey(compositeKey.getGuildID()))
//                usernames.put(compositeKey.getGuildID(), new HashMap<>());
//            usernames.get(compositeKey.getGuildID()).put(compositeKey.getUserID(), permissionLevel);
        }

        JSONObject jsonObject = new JSONObject(usernames);
        try (FileWriter fileWriter = new FileWriter("unhistory.json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved unhistory.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadUsernameHistoryConfig(File file) {
        if (!file.exists())
        {
           boolean didMake = file.mkdir();
           if (didMake)
            Logger.getLogger().print("Created unhistory.json");
           else
               Logger.getLogger().print("Failed to create unhistory.json");
        }
        try {
            Map<String, HashMap<String, String>> usernameMap = new ObjectMapper().readValue(file, HashMap.class);
            UsernameHistoryCommand.setUsernames(usernameMap);
            Logger.getLogger().print("Loaded unhistory.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
