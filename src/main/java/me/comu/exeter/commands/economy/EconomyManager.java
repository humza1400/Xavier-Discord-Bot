package me.comu.exeter.commands.economy;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;

public class EconomyManager {

    private static  HashMap<Member, Double> users = new HashMap<Member, Double>();

    public EconomyManager(Guild guild)
    {
        for (Member member : guild.getMembers())
        {
            users.put(member, 0.0);
        }
    }

    public static void setBalance(Member user, double amount)
    {
        users.put(user, amount);
    }

    public static double getBalance(Member user)
    {
        return users.get(user);
    }
    // Used to establish members in the hashmap that are not yet established; should prevent potential errors
    public static boolean verifyMember(Member member)
    {
        return !users.containsKey(member);
    }

    public static HashMap<Member, Double> getUsers()
    {
        return users;
    }

    // Used for loading ocnfig
    public static HashMap<Member, Double> setUsers(HashMap<Member, Double> hashMap)
    {
        return users = hashMap;
    }
}
