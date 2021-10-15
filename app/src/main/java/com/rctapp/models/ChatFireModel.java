package com.rctapp.models;

public class ChatFireModel {
    String quote_id, sender_id, receiver_id, message;
    boolean readStatus;
    long time;

    public ChatFireModel() {
    }

    public ChatFireModel(String quote_id, String sender_id, String receiver_id, String message, boolean readStatus, long time) {
        this.quote_id = quote_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.readStatus = readStatus;
        this.time = time;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatFireModel{" +
                "quote_id='" + quote_id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", receiver_id='" + receiver_id + '\'' +
                ", message='" + message + '\'' +
                ", readStatus=" + readStatus +
                ", time=" + time +
                '}';
    }
}
