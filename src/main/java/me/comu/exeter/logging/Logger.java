package me.comu.exeter.logging;

public  final class Logger {

    private static Logger logger = null;

    public void print(String message) {
        System.out.println(String.format("%s", message));
    }

    public static Logger getLogger() {
        return logger == null ? logger = new Logger() : logger;
    }

}
