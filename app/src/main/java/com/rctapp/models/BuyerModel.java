package com.rctapp.models;

public class BuyerModel {
    String id, name, countryName, phoneNumber, image;
    boolean activeStatus;

    public BuyerModel() {
    }

    public BuyerModel(String id, String name, String countryName, String phoneNumber, String image, boolean activeStatus) {
        this.id = id;
        this.name = name;
        this.countryName = countryName;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.activeStatus = activeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    @Override
    public String toString() {
        return "BuyerModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", countryName='" + countryName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", image='" + image + '\'' +
                ", activeStatus=" + activeStatus +
                '}';
    }
}

