package com.stem.chatcake.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityHomeBinding;
import com.stem.chatcake.service.SocketService;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity {

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        viewModel = HomeViewModel.builder()
                .host(this)
                .storageService(StorageService.getInstance(this))
                .socketService(SocketService.getInstance())
                .fragmentManager(getSupportFragmentManager())
                .build();
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.init();
    }
}