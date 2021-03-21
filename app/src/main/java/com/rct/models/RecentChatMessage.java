package com.rct.models;

public class RecentChatMessage {
    String quoteId, senderId, receiverId, message;

    public RecentChatMessage() {
    }

    public RecentChatMessage(String quoteId, String senderId, String receiverId, String message) {
        this.quoteId = quoteId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RecentChatMessage{" +
                "quoteId='" + quoteId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
