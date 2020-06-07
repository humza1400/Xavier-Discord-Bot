package me.comu.exeter.objects;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import java.util.EnumSet;

public class RestorableRole {

    private String name;
    private String id;
    private int position;
    private boolean mentionable;
    private boolean hoisted;
    private int color;
    private EnumSet<Permission> permissionCollection;

    public RestorableRole(Role role) {
        this.name = role.getName();
        this.id = role.getId();
        this.position = role.getPosition();
        this.mentionable = role.isMentionable();
        this.hoisted = role.isHoisted();
        this.color = role.getColorRaw();
        this.permissionCollection = role.getPermissions();
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public boolean isMentionable() {
        return mentionable;
    }

    public boolean isHoisted() {
        return hoisted;
    }

    public int getColor() {
        return color;
    }

    public EnumSet<Permission> getPermissions()
    {
        return permissionCollection;
    }

    public int getPosition() {
        return position;
    }


}

