package com.stem.chatcake.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Message {

    private String member;
    private String content;

    public Message(String member, String content) {
        this.member = member;
        this.content = content;
    }
}
