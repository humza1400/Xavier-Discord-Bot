package me.comu.exeter.commands.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.logging.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class WhitelistedJSONHandler {



        public static void saveWhitelistConfig()
        {
            JSONObject jsonObject = new JSONObject(WhitelistCommand.getWhitelistedIDs());
            try(FileWriter fileWriter = new FileWriter("whitelisted.json")){
                fileWriter.write(jsonObject.toString());
                fileWriter.flush();
                fileWriter.close();
                Logger.getLogger().print("Saved whitelisted.json");
            }
            catch (IOException e)
            {e.printStackTrace();
            }
        }
        public static void loadWhitelistConfig(File file)
        {
            try {
                HashMap<String, String> whitelistedJSON = new ObjectMapper().readValue(file, HashMap.class);
                WhitelistCommand.setWhitelistedIDs(whitelistedJSON);
                Logger.getLogger().print("Loaded whitelisted.json");
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }
