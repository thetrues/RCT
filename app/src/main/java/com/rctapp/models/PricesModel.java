package com.rctapp.models;

public class PricesModel {
    String id, region, district, variety;
    int grade;
    String date;
    int active;
    float price_rate;


    public PricesModel() {
    }

    public PricesModel(String id, String region, String district, String variety, int grade, String date, int active, float price_rate) {
        this.id = id;
        this.region = region;
        this.district = district;
        this.variety = variety;
        this.grade = grade;
        this.date = date;
        this.active = active;
        this.price_rate = price_rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public float getPrice_rate() {
        return price_rate;
    }

    public void setPrice_rate(float price_rate) {
        this.price_rate = price_rate;
    }

    @Override
    public String toString() {
        return "PricesModel{" +
                "id='" + id + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", variety='" + variety + '\'' +
                ", grade=" + grade +
                ", date='" + date + '\'' +
                ", active=" + active +
                ", price_rate=" + price_rate +
                '}';
    }
}
