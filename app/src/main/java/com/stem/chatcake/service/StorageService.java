package com.stem.chatcake.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.stem.chatcake.model.Room;
import com.stem.chatcake.model.User;

import java.util.List;

public class StorageService {

    private static StorageService instance = null;
    private SharedPreferences sharedPreferences;
    private Gson converter = new Gson();

    private StorageService (Context context) {
        this.sharedPreferences = context.getSharedPreferences("chatcake", Context.MODE_PRIVATE);
    }

    public static StorageService getInstance(Context context) {
        if (instance == null)
            instance = new StorageService(context);
        return instance;
    }

    public void saveUserInfo (User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", user.getId());
        editor.putString("name",  user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("token", user.getToken());
        editor.apply();
    }

    public User getUserInfo () {
        User user = User.builder()
            .id(sharedPreferences.getString("id", null))
            .name(sharedPreferences.getString("name", null))
            .username(sharedPreferences.getString("username", null))
            .token(sharedPreferences.getString("token", null)).build();
        return user;
    }

    public String getUsername () {
        return sharedPreferences.getString("username", null);
    }

    public String getToken () {
        return sharedPreferences.getString("token", null);
    }

    public boolean loggedIn () {
        return sharedPreferences.contains("token");
    }

    private void saveRooms (List<Room> rooms) {
        String roomsString = converter.toJson(rooms);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rooms", roomsString);
        editor.apply();
    }

    private List<Room> getRooms () {
        return (List<Room>) converter.fromJson(sharedPreferences.getString("rooms", null), List.class);
    }

}
