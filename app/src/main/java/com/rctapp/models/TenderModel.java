package com.rctapp.models;

public class TenderModel {
    String id, quantity, grade, variety, active, buyer_id,
            description, seller_selection, dateCreated;

    public TenderModel() {
    }

    public TenderModel(String id, String quantity, String grade, String variety, String active, String buyer_id, String description, String seller_selection, String dateCreated) {
        this.id = id;
        this.quantity = quantity;
        this.grade = grade;
        this.variety = variety;
        this.active = active;
        this.buyer_id = buyer_id;
        this.description = description;
        this.seller_selection = seller_selection;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeller_selection() {
        return seller_selection;
    }

    public void setSeller_selection(String seller_selection) {
        this.seller_selection = seller_selection;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "TenderModel{" +
                "id='" + id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", grade='" + grade + '\'' +
                ", variety='" + variety + '\'' +
                ", active='" + active + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", description='" + description + '\'' +
                ", seller_selection='" + seller_selection + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                '}';
    }
}

