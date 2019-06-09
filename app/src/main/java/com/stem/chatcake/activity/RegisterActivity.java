package com.stem.chatcake.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityRegisterBinding;
import com.stem.chatcake.service.ConnectionService;
import com.stem.chatcake.service.HttpService;
import com.stem.chatcake.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        // dependency injection
        RegisterViewModel viewModel = RegisterViewModel.builder()
                .context(this)
                .httpService(HttpService.getInstance())
                .connectionService(ConnectionService.getInstance())
                .build();
        binding.setViewModel(viewModel);
    }
}
