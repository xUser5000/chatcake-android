package com.stem.chatcake.model;

public class User {

    private String token;
    private String id;
    private String name;
    private String username;
    private String password;

    // default constructor
    public User() {
    }

    // login constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // register constructor
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // general constructor
    public User(String id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
