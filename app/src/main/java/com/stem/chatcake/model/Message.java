package com.stem.chatcake.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Message {

    private String from;
    private String content;
    private String roomId;

}
