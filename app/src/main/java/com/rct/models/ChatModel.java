package com.rct.models;

public class ChatModel {
    String id, name, lastMessage, time;

    public ChatModel() {
    }

    public ChatModel(String id, String name, String lastMessage, String time) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
