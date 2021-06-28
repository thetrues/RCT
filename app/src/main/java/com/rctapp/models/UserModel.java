package com.rctapp.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    String id, token, names, role, phone, dial_code, img;
    int active;

    public UserModel() {
    }

    public UserModel(String id, String token, String names, String role, String phone, String dial_code, String img, int active) {
        this.id = id;
        this.token = token;
        this.names = names;
        this.role = role;
        this.phone = phone;
        this.dial_code = dial_code;
        this.img = img;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", names='" + names + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                ", dial_code='" + dial_code + '\'' +
                ", img='" + img + '\'' +
                ", active=" + active +
                '}';
    }
}