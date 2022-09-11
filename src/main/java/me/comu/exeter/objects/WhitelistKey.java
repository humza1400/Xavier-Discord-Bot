package me.comu.exeter.objects;

import java.util.Objects;

public class WhitelistKey {

    private final String guildID;
    private final String userID;

    public WhitelistKey(String guildID, String userID) {
        this.guildID = guildID;
        this.userID = userID;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getUserID() {
        return userID;
    }

    public static WhitelistKey of(String guildID, String userID) {
        return new WhitelistKey(guildID, userID);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhitelistKey that = (WhitelistKey) o;
        return Objects.equals(guildID, that.guildID) &&
                Objects.equals(userID, that.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildID, userID);
    }

    @Override
    public String toString() {
        return "CompositeKey{" + "guildID='" + guildID + '\'' + ", userID='" + userID + '\'' + '}';
    }
}

