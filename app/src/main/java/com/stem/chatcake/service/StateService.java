package com.stem.chatcake.service;

import com.stem.chatcake.model.Room;

import java.util.List;

public class StateService {

    private static StateService instance = null;

    private Room data;

    private StateService() {}

    public static StateService getInstance () {
        if (instance == null) instance = new StateService();
        return instance;
    }

    public void setData(Room data) {
        this.data = data;
    }

    public Room getData() {
        return data;
    }

    public void addMember (String username) {
        List<String> members = data.getMembers();
        members.add(username);
        data.setMembers(members);
    }

    public void removeMember (String username) {
        List<String> members = data.getMembers();
        members.remove(username);
        data.setMembers(members);
    }

}
