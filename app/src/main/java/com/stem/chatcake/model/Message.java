package com.stem.chatcake.model;

public class Message {

    private String member;
    private String content;

    public Message() {
    }

    public Message(String member, String content) {
        this.member = member;
        this.content = content;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
