package com.stem.chatcake.model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String id;
    private String name;
    private String admin;
    private List<String> members;
    private List<Message> messages;

    public Room() {
    }

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
        this.messages = new ArrayList<>();
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return name;
    }
}
