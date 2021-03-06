package com.stem.chatcake.viewmodel;

import android.app.Activity;
import android.content.Intent;

import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.activity.HomeActivity;
import com.stem.chatcake.activity.LoginActivity;
import com.stem.chatcake.activity.RegisterActivity;

import lombok.Builder;

@Builder
public class MainViewModel {

    // dependencies
    private Activity host;
    private LocalStorageService localStorageService;

    public void init () {
        // if the user is logged in, redirect them to the home page
        if (localStorageService.loggedIn()) {
            host.startActivity(new Intent(host, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            host.finish();
        }
    }

    public void goLogin () {
        host.startActivity(new Intent(host, LoginActivity.class));
    }

    public void goRegister () {
        host.startActivity(new Intent(host, RegisterActivity.class));
    }
}
