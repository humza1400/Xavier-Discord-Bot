package me.comu.exeter.commands.economy;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class EcoJSONHandler {

     public static void saveEconomyConfig()
    {
        JSONObject jsonObject = new JSONObject(EconomyManager.getUsers());
        try(FileWriter fileWriter = new FileWriter("economy.json")){
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
            Logger.getLogger().print("Saved economy.json");
        }
        catch (IOException e)
        {e.printStackTrace();
        }
    }
    public static void loadEconomyConfig(File file)
    {
        try {
            HashMap<String, Integer> userDoubleHashMap = new ObjectMapper().readValue(file, HashMap.class);
            EconomyManager.setUsers(userDoubleHashMap);
            Logger.getLogger().print("Loaded economy.json");
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

}
