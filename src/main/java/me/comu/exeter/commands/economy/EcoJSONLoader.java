package me.comu.exeter.commands.economy;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class EcoJSONLoader {

     public static void saveEconomyConfig()
    {
        JSONObject jsonObject = new JSONObject(EconomyManager.getUsers());
//        JSONArray list = new JSONArray();
//        list.put("test");
//        jsonObject.put("economy", list);
        try(FileWriter fileWriter = new FileWriter("src/main/java/me/comu/exeter/commands/economy/economy.json")){
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            Logger.getLogger().print("Saved economy.json");
        }
        catch (IOException e)
        {e.printStackTrace();
        }
    }
    public static void loadEconomyConfig(File file)
    {
        try {
            EconomyManager.setUsers(new ObjectMapper().readValue(file, HashMap.class));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

}
