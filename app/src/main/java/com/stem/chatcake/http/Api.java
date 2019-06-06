package com.stem.chatcake.http;

import com.stem.chatcake.model.Room;
import com.stem.chatcake.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    // register a new user
    @POST("auth/register")
    Call<ResponseBody> register (@Body User user);

    // login
    @POST("auth/login")
    Call<User> login (@Body User user);

    // log out
    @DELETE("auth/logout")
    Call<ResponseBody> logout (@Header("Authorization") String token);

    // create a new room
    @POST("room/create/{roomName}")
    Call<Room> createRoom (
            @Header("Authorization") String token,
            @Path("roomName") String roomName
    );

    // get the rooms for a specific user
    @GET("user/getRooms")
    Call<List<Room>> getRooms (@Header("Authorization") String token);

    // get all the info about one room (including admin, members, messages) using its id
    @GET("room/getInfo/{roomId}")
    Call<Room> getRoomInfo (
            @Header("Authorization") String token,
            @Path("roomId") String roomId
    );

}
