package com.rctapp.models;

public class ChatInitVar {
    String seller_id, buyer_id, quote_id;
    boolean status;
    long time;

    public ChatInitVar() {
    }

    public ChatInitVar(String seller_id, String buyer_id, String quote_id, boolean status, long time) {
        this.seller_id = seller_id;
        this.buyer_id = buyer_id;
        this.quote_id = quote_id;
        this.status = status;
        this.time = time;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatInitVar{" +
                "seller_id='" + seller_id + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", quote_id='" + quote_id + '\'' +
                ", status=" + status +
                ", time=" + time +
                '}';
    }
}
