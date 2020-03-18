package me.comu.exeter.util;

import java.util.HashMap;

public class VCTrackingManager {

    private static HashMap<String, Long> joinTimes = new HashMap<String, Long>();
    private static HashMap<String, Long> leaveTimes = new HashMap<String, Long>();

    public static void setJoinTime(String user, Long amount) {
        joinTimes.put(user, amount);
    }

    public static long getJoinTime(String user) {
        return joinTimes.get(user);
    }

    public static void resetAllJoinTimes()
    {
        joinTimes.replaceAll((k,v) -> v=0L);
    }

    public static void removeJoinedUser(String user)
    {
        joinTimes.remove(user);
    }

    public static boolean verifyJoinedUser(String user) {
        return !joinTimes.containsKey(user);
    }

    public static HashMap<String, Long> getJoinedVCUsers() {
        return joinTimes;
    }

    public static HashMap<String, Long> setJoinedVCUsers(HashMap<String, Long> hashMap) {
        return joinTimes = hashMap;
    }

    //
    public static void setLeaveTime(String user, Long amount) {
        leaveTimes.put(user, amount);
    }

    public static long getLeaveTime(String user) {
        return leaveTimes.get(user);
    }

    public static void resetAllLeaveTimes()
    {
        leaveTimes.replaceAll((k,v) -> v=0L);
    }

    public static boolean verifyLeaveUser(String user) {
        return !leaveTimes.containsKey(user);
    }

    public static void removeLeaveUser(String user)
    {
        leaveTimes.remove(user);
    }

    public static HashMap<String, Long> getLeaveVCUsers() {
        return leaveTimes;
    }

    public static HashMap<String, Long> setLeaveVCUsers(HashMap<String, Long> hashMap) {
        return leaveTimes = hashMap;
    }
}
