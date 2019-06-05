package com.stem.chatcake.service;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stem.chatcake.model.Message;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class SocketService {

    private static SocketService instance = null;

    private Socket socket;
    private Gson gson;

    private boolean connected = false;
    private boolean authorized = false;
    private ArrayList<String> subscribedRooms = new ArrayList<>();
    private final String URI = "http://192.168.1.4:5000";

    // private constructor
    private SocketService () {
        gson = new Gson();
    }

    public static SocketService getInstance() {
        if (instance == null) instance = new SocketService();
        return instance;
    }

    // initialize the connection
    public void connect () {
        if (connected) return;

        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.forceNew = true;
        try {
            socket = IO.socket(URI, options);
            socket.connect();
            connected = true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // authenticate
    public void authenticate (final Activity host, String token, final Runnable runnable) {
        if (authorized) return;

        socket.emit("authorize", token);
        socket.on("authorized", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                authorized = true;
                host.runOnUiThread(runnable);
            }
        });
    }

    public void subscribe (String roomId, final Activity host, final OnMessageReceivedListener onMessageReceivedListener) {
        if (subscribedRooms.contains(roomId)) return;

        socket.emit("subscribe", roomId);
        subscribedRooms.add(roomId);

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                host.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JsonObject object = (JsonObject) args[0];
                        Message message = Message.builder()
                                .roomId(object.get("roomId").toString())
                                .content(object.get("content").toString())
                                .from(object.get("from").toString())
                                .build();

                        onMessageReceivedListener.messageReceived(message);
                    }
                });
            }
        });
    }

    public void sendMessage (Message message) {
        String stringMessage = gson.toJson(message);
        socket.emit("message", stringMessage);
    }

    public interface OnMessageReceivedListener {
        void messageReceived (Message message);
    }

}
