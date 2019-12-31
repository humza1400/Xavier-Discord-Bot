package me.comu.exeter.commands.economy;

import java.util.HashMap;

public class EconomyManager {

    private static HashMap<String, Integer> users = new HashMap<String, Integer>();

//    public EconomyManager(Guild guild)
//    {
//        for (Member member : guild.getMembers())
//        {
//            users.put(member, 0.0);
//        }
//    }

    public static void setBalance(String user, int amount) {
        users.put(user, amount);
    }

    public static int getBalance(String user) {
        return users.get(user);
    }

    // Used to establish members in the hashmap that are not yet established; should prevent potential errors
    public static boolean verifyUser(String user) {
        return !users.containsKey(user);
    }

    public static HashMap<String, Integer> getUsers() {
        return users;
    }

    // Used for loading ocnfig
    public static HashMap<String, Integer> setUsers(HashMap<String, Integer> hashMap) {
        return users = hashMap;
    }
}
