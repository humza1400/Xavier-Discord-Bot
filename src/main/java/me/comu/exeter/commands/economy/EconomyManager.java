package me.comu.exeter.commands.economy;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;

public class EconomyManager {

    private static final HashMap<Member, Double> users = new HashMap<Member, Double>();

//    public EconomyManager(Guild guild)
//    {
//        for (Member member : guild.getMembers())
//        {
//            users.put(member, 0.0);
//        }
//    }

    public static void setBalance(Member user, double amount)
    {
        users.put(user, amount);
    }

    public static double getBalance(Member user)
    {
        return users.get(user);
    }
    // no use
    public static boolean verifyMember(Member member)
    {
        if (users.containsKey(member))
            return false;
        return true;
    }

    public static HashMap<Member, Double> getUsers()
    {
        return users;
    }
}
