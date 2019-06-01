package com.stem.chatcake.model;


import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Room {

    private String id;
    private String name;
    private String admin;
    private List<String> members;
    private List<Message> messages;

    @Override
    public String toString() {
        return name;
    }
}
