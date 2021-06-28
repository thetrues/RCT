package com.rctapp.models;

public class VarietyVar {
    String id;
    int active;
    String name, platformName, region, district, userName;

    public VarietyVar() {
    }

    public VarietyVar(String id, int active, String name, String platformName, String region, String district, String userName) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.platformName = platformName;
        this.region = region;
        this.district = district;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "VarietyVar{" +
                "id='" + id + '\'' +
                ", active=" + active +
                ", name='" + name + '\'' +
                ", platformName='" + platformName + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
