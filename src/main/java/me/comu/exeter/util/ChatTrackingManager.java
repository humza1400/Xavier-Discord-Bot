package me.comu.exeter.util;

import java.util.HashMap;

public class ChatTrackingManager {

    private static HashMap<String, Integer> chatTrack = new HashMap<>();

    public static void setChatCredits(String user, Integer amount) {
        chatTrack.put(user, amount);
    }

    public static int getChatCredits(String user) {
        return chatTrack.get(user);
    }

    public static void resetAllChatCredits() {
        chatTrack.replaceAll((k, v) -> v = 0);
    }

    public static boolean verifyChatUser(String user) {
        return !chatTrack.containsKey(user);
    }

    public static void removeChatUser(String user) { chatTrack.remove(user);}

    public static HashMap<String, Integer> getChatUsers() {
        return chatTrack;
    }

    public static HashMap<String, Integer> setChatUsers(HashMap<String, Integer> hashMap) {
        return chatTrack = hashMap;
    }

}
