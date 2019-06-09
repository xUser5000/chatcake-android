package com.stem.chatcake.service;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.Gson;
import com.stem.chatcake.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class SocketService {

    private static SocketService instance = null;

    private Socket socket;
    private Gson gson;

    private boolean connected = false;
    private boolean authorized = false;
    private final String URI = "http://" + RemoteConstants.HOST + ":5000";

    // private constructor
    private SocketService () {
        gson = new Gson();
    }

    public static SocketService getInstance() {
        if (instance == null) instance = new SocketService();
        return instance;
    }

    // initialize the connection
    public void connect (Runnable runnable) {
        if (connected) return;

        IO.Options options = new IO.Options();
        options.reconnection = true;
        options.forceNew = true;
        try {
            socket = IO.socket(URI, options);
            socket.connect();
            connected = true;
            runnable.run();
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

    // notify observers on each event:
    public void subscribe (final Activity host, final Object viewModel) {
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                host.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];

                        try {

                            Message message = Message.builder()
                                    .from(data.getString("from"))
                                    .content(data.getString("content"))
                                    .roomId(data.getString("roomId"))
                                    .build();
                            OnMessageReceivedListener listener = (OnMessageReceivedListener) viewModel;
                            listener.OnMessageReceived(message);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

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
        void OnMessageReceived (Message message);
    }
}
