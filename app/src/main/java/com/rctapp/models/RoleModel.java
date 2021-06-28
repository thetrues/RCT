package com.rctapp.models;

public class RoleModel {
    String id, role, user_id;
            int active;

    public RoleModel() {
    }

    public RoleModel(String id, String role, String user_id, int active) {
        this.id = id;
        this.role = role;
        this.user_id = user_id;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", user_id='" + user_id + '\'' +
                ", active=" + active +
                '}';
    }
}
