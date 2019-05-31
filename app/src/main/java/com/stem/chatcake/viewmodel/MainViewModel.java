package com.stem.chatcake.viewmodel;

import android.content.Context;
import android.content.Intent;

import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.view.HomeActivity;
import com.stem.chatcake.view.LoginActivity;
import com.stem.chatcake.view.RegisterActivity;

public class MainViewModel {

    private Context context;
    private StorageService storageService;

    public MainViewModel(Context context) {
        this.context = context;
        storageService = StorageService.getInstance(context);
        if (storageService.loggedIn()) {
            context.startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    public void goLogin () {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void goRegister () {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }
}
