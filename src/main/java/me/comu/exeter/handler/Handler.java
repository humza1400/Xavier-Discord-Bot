package me.comu.exeter.handler;

import java.io.File;

public abstract class Handler {

    public Handler(File file) {
        loadConfig(file);
    }
    public abstract void saveConfig();
    public abstract void loadConfig(File file);

}
