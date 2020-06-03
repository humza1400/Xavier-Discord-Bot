package me.comu.exeter.util;

import java.util.Objects;

public class CompositeKey {

    private String guildID;
    private String userID;

    public CompositeKey(String guildID, String userID) {
        this.guildID = guildID;
        this.userID = userID;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getUserID() {
        return userID;
    }

    public static CompositeKey of(String guildID, String userID) {
        return new CompositeKey(guildID, userID);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
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

