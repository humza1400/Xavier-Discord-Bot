package me.comu.exeter.logging;

import me.comu.exeter.core.Core;

public  final class Logger {

    private static Logger logger = null;

    public void print(String message) {
        System.out.println(String.format("%s %s", Core.DEBUG, message));
    }

    public static Logger getLogger() {
        return logger == null ? logger = new Logger() : logger;
    }

}
