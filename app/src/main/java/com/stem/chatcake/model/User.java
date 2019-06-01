package com.stem.chatcake.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

    private String token;
    private String id;
    private String name;
    private String username;
    private String password;

}
