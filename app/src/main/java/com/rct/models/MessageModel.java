package com.rct.models;

public class MessageModel {
    String id, messengerId, senderId, receiverId, message;
    long time;
    boolean readStatus;

    public MessageModel() {
    }

    public MessageModel(String id, String messengerId, String senderId, String receiverId, String message, long time, boolean readStatus) {
        this.id = id;
        this.messengerId = messengerId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.time = time;
        this.readStatus = readStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessengerId() {
        return messengerId;
    }

    public void setMessengerId(String messengerId) {
        this.messengerId = messengerId;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", messengerId='" + messengerId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", readStatus=" + readStatus +
                '}';
    }
}
