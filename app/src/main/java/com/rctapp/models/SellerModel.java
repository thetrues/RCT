package com.rctapp.models;

public class SellerModel {
    String id, category, full_name, dail_code,
    phone_number, platform_name, platform_leader,
    location, card_type, application_type, is_tbs_certified, image_path, certification_path, card_path;
    int is_graded;

    public SellerModel() {
    }

    public SellerModel(String id, String category, String full_name, String dail_code, String phone_number, String platform_name, String platform_leader, String location, String card_type, String application_type, String is_tbs_certified, String image_path, String certification_path, String card_path, int is_graded) {
        this.id = id;
        this.category = category;
        this.full_name = full_name;
        this.dail_code = dail_code;
        this.phone_number = phone_number;
        this.platform_name = platform_name;
        this.platform_leader = platform_leader;
        this.location = location;
        this.card_type = card_type;
        this.application_type = application_type;
        this.is_tbs_certified = is_tbs_certified;
        this.image_path = image_path;
        this.certification_path = certification_path;
        this.card_path = card_path;
        this.is_graded = is_graded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDail_code() {
        return dail_code;
    }

    public void setDail_code(String dail_code) {
        this.dail_code = dail_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public String getPlatform_leader() {
        return platform_leader;
    }

    public void setPlatform_leader(String platform_leader) {
        this.platform_leader = platform_leader;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getApplication_type() {
        return application_type;
    }

    public void setApplication_type(String application_type) {
        this.application_type = application_type;
    }

    public String getIs_tbs_certified() {
        return is_tbs_certified;
    }

    public void setIs_tbs_certified(String is_tbs_certified) {
        this.is_tbs_certified = is_tbs_certified;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getCertification_path() {
        return certification_path;
    }

    public void setCertification_path(String certification_path) {
        this.certification_path = certification_path;
    }

    public String getCard_path() {
        return card_path;
    }

    public void setCard_path(String card_path) {
        this.card_path = card_path;
    }

    public int getIs_graded() {
        return is_graded;
    }

    public void setIs_graded(int is_graded) {
        this.is_graded = is_graded;
    }

    @Override
    public String toString() {
        return "SellerModel{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", full_name='" + full_name + '\'' +
                ", dail_code='" + dail_code + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", platform_name='" + platform_name + '\'' +
                ", platform_leader='" + platform_leader + '\'' +
                ", location='" + location + '\'' +
                ", card_type='" + card_type + '\'' +
                ", application_type='" + application_type + '\'' +
                ", is_tbs_certified='" + is_tbs_certified + '\'' +
                ", image_path='" + image_path + '\'' +
                ", certification_path='" + certification_path + '\'' +
                ", card_path='" + card_path + '\'' +
                ", is_graded=" + is_graded +
                '}';
    }
}


