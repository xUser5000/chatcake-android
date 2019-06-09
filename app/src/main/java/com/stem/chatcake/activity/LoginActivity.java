package com.stem.chatcake.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityLoginBinding;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel viewModel = LoginViewModel.builder()
                .host(this)
                .httpService(HttpService.getInstance())
                .localStorageService(LocalStorageService.getInstance(this))
                .connectionService(ConnectionService.getInstance())
                .build();
        binding.setViewModel(viewModel);
    }
}
