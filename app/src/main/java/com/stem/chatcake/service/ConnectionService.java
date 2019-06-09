package com.stem.chatcake.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionService {

    private static ConnectionService instance = null;

    private ConnectionService () {
    }

    public static ConnectionService getInstance() {
        if (instance == null) instance = new ConnectionService();
        return instance;
    }

    public boolean getConnectionState (Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public void showMessage (Context context) {
        String MESSAGE = "Check your internet connection and try again...";
        Toast.makeText(context, MESSAGE, Toast.LENGTH_SHORT).show();
    }
}
