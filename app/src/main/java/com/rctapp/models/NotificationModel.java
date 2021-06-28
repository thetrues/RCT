package com.rctapp.models;

import java.io.Serializable;

public class NotificationModel  implements Serializable {
    String id, seller_id, variety, pickup_location, extra_details, buyer_selection, image_string,
            id_string, request_document, request_card, product_images_list;
    long quantity, grade, active, selling_price;
    boolean graded;

    public NotificationModel() {
    }

    public NotificationModel(String id, String seller_id, String variety, String pickup_location, String extra_details, String buyer_selection, String image_string, String id_string, String request_document, String request_card, String product_images_list, long quantity, long grade, long active, long selling_price, boolean graded) {
        this.id = id;
        this.seller_id = seller_id;
        this.variety = variety;
        this.pickup_location = pickup_location;
        this.extra_details = extra_details;
        this.buyer_selection = buyer_selection;
        this.image_string = image_string;
        this.id_string = id_string;
        this.request_document = request_document;
        this.request_card = request_card;
        this.product_images_list = product_images_list;
        this.quantity = quantity;
        this.grade = grade;
        this.active = active;
        this.selling_price = selling_price;
        this.graded = graded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getExtra_details() {
        return extra_details;
    }

    public void setExtra_details(String extra_details) {
        this.extra_details = extra_details;
    }

    public String getBuyer_selection() {
        return buyer_selection;
    }

    public void setBuyer_selection(String buyer_selection) {
        this.buyer_selection = buyer_selection;
    }

    public String getImage_string() {
        return image_string;
    }

    public void setImage_string(String image_string) {
        this.image_string = image_string;
    }

    public String getId_string() {
        return id_string;
    }

    public void setId_string(String id_string) {
        this.id_string = id_string;
    }

    public String getRequest_document() {
        return request_document;
    }

    public void setRequest_document(String request_document) {
        this.request_document = request_document;
    }

    public String getRequest_card() {
        return request_card;
    }

    public void setRequest_card(String request_card) {
        this.request_card = request_card;
    }

    public String getProduct_images_list() {
        return product_images_list;
    }

    public void setProduct_images_list(String product_images_list) {
        this.product_images_list = product_images_list;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public long getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(long selling_price) {
        this.selling_price = selling_price;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "id='" + id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", variety='" + variety + '\'' +
                ", pickup_location='" + pickup_location + '\'' +
                ", extra_details='" + extra_details + '\'' +
                ", buyer_selection='" + buyer_selection + '\'' +
                ", image_string='" + image_string + '\'' +
                ", id_string='" + id_string + '\'' +
                ", request_document='" + request_document + '\'' +
                ", request_card='" + request_card + '\'' +
                ", product_images_list='" + product_images_list + '\'' +
                ", quantity=" + quantity +
                ", grade=" + grade +
                ", active=" + active +
                ", selling_price=" + selling_price +
                ", graded=" + graded +
                '}';
    }
}