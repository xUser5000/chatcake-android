package com.stem.chatcake.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityLoginBinding;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel viewModel = LoginViewModel.builder()
                .context(this)
                .httpService(HttpService.getInstance())
                .storageService(StorageService.getInstance(this))
                .build();
        binding.setViewModel(viewModel);
    }
}
