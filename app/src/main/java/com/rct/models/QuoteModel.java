package com.rct.models;

public class QuoteModel {
    String id, tender_id, seller_id, supply_details;
    long supply_quantity, supply_price, active;

    public QuoteModel() {
    }

    public QuoteModel(String id, String tender_id, String seller_id, String supply_details, long supply_quantity, long supply_price, long active) {
        this.id = id;
        this.tender_id = tender_id;
        this.seller_id = seller_id;
        this.supply_details = supply_details;
        this.supply_quantity = supply_quantity;
        this.supply_price = supply_price;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTender_id() {
        return tender_id;
    }

    public void setTender_id(String tender_id) {
        this.tender_id = tender_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSupply_details() {
        return supply_details;
    }

    public void setSupply_details(String supply_details) {
        this.supply_details = supply_details;
    }

    public long getSupply_quantity() {
        return supply_quantity;
    }

    public void setSupply_quantity(long supply_quantity) {
        this.supply_quantity = supply_quantity;
    }

    public long getSupply_price() {
        return supply_price;
    }

    public void setSupply_price(long supply_price) {
        this.supply_price = supply_price;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "QuoteModel{" +
                "id='" + id + '\'' +
                ", tender_id='" + tender_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", supply_details='" + supply_details + '\'' +
                ", supply_quantity=" + supply_quantity +
                ", supply_price=" + supply_price +
                ", active=" + active +
                '}';
    }
}
