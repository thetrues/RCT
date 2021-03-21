package com.rct.models;

public class PlatformModel {
    String id, country, platform_name, platform_country_dial_code, phone_number,
    platform_region, leader_name, image_path;
    int active, number_of_member;

    public PlatformModel() {
    }

    public PlatformModel(String id, String country, String platform_name, String platform_country_dial_code, String phone_number, String platform_region, String leader_name, String image_path, int active, int number_of_member) {
        this.id = id;
        this.country = country;
        this.platform_name = platform_name;
        this.platform_country_dial_code = platform_country_dial_code;
        this.phone_number = phone_number;
        this.platform_region = platform_region;
        this.leader_name = leader_name;
        this.image_path = image_path;
        this.active = active;
        this.number_of_member = number_of_member;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public String getPlatform_country_dial_code() {
        return platform_country_dial_code;
    }

    public void setPlatform_country_dial_code(String platform_country_dial_code) {
        this.platform_country_dial_code = platform_country_dial_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPlatform_region() {
        return platform_region;
    }

    public void setPlatform_region(String platform_region) {
        this.platform_region = platform_region;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getNumber_of_member() {
        return number_of_member;
    }

    public void setNumber_of_member(int number_of_member) {
        this.number_of_member = number_of_member;
    }

    @Override
    public String toString() {
        return "PlatformModel{" +
                "id='" + id + '\'' +
                ", country='" + country + '\'' +
                ", platform_name='" + platform_name + '\'' +
                ", platform_country_dial_code='" + platform_country_dial_code + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", platform_region='" + platform_region + '\'' +
                ", leader_name='" + leader_name + '\'' +
                ", image_path='" + image_path + '\'' +
                ", active=" + active +
                ", number_of_member=" + number_of_member +
                '}';
    }
}
