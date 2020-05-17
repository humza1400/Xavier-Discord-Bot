package me.comu.exeter.core;

import io.github.cdimascio.dotenv.Dotenv;
import me.comu.exeter.logging.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static boolean createDirectory(String path) {
        File file = new File(path);
        return file.mkdirs();
    }

    public static boolean doesExist(String path) {
        File file = new File(path);
        return (file.exists());

    }

    public static void buildDirectory(String path, String name) {
        if (doesExist(path)) {
            Logger.getLogger().print("Running on pre-" + name + " directory");
        } else {
            createDirectory(path);
            Logger.getLogger().print("Created " + name + " directory");
        }
    }

    public static void clearCacheDirectory()
    {
        try {
            File file = new File("cache");
            FileUtils.cleanDirectory(file);
        } catch (IOException e)
        {
         e.printStackTrace();
        }
    }

}
