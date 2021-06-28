package com.rctapp.models;

import java.io.Serializable;

public class ChatModel implements Serializable {
    String messengerId, messenger_id, seller_id, quote_id, buyer, seller;
    boolean chat_status, expiration_status;
    int message_count;
    String buyer_image_path, seller_image_path;
    long time;

    public ChatModel() {
    }

    public ChatModel(String messengerId, String messenger_id, String seller_id, String quote_id, String buyer, String seller, boolean chat_status, boolean expiration_status, int message_count, String buyer_image_path, String seller_image_path, long time) {
        this.messengerId = messengerId;
        this.messenger_id = messenger_id;
        this.seller_id = seller_id;
        this.quote_id = quote_id;
        this.buyer = buyer;
        this.seller = seller;
        this.chat_status = chat_status;
        this.expiration_status = expiration_status;
        this.message_count = message_count;
        this.buyer_image_path = buyer_image_path;
        this.seller_image_path = seller_image_path;
        this.time = time;
    }

    public String getMessengerId() {
        return messengerId;
    }

    public void setMessengerId(String messengerId) {
        this.messengerId = messengerId;
    }

    public String getMessenger_id() {
        return messenger_id;
    }

    public void setMessenger_id(String messenger_id) {
        this.messenger_id = messenger_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public boolean isChat_status() {
        return chat_status;
    }

    public void setChat_status(boolean chat_status) {
        this.chat_status = chat_status;
    }

    public boolean isExpiration_status() {
        return expiration_status;
    }

    public void setExpiration_status(boolean expiration_status) {
        this.expiration_status = expiration_status;
    }

    public int getMessage_count() {
        return message_count;
    }

    public void setMessage_count(int message_count) {
        this.message_count = message_count;
    }

    public String getBuyer_image_path() {
        return buyer_image_path;
    }

    public void setBuyer_image_path(String buyer_image_path) {
        this.buyer_image_path = buyer_image_path;
    }

    public String getSeller_image_path() {
        return seller_image_path;
    }

    public void setSeller_image_path(String seller_image_path) {
        this.seller_image_path = seller_image_path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "messengerId='" + messengerId + '\'' +
                ", messenger_id='" + messenger_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", quote_id='" + quote_id + '\'' +
                ", buyer='" + buyer + '\'' +
                ", seller='" + seller + '\'' +
                ", chat_status=" + chat_status +
                ", expiration_status=" + expiration_status +
                ", message_count=" + message_count +
                ", buyer_image_path='" + buyer_image_path + '\'' +
                ", seller_image_path='" + seller_image_path + '\'' +
                ", time=" + time +
                '}';
    }
}