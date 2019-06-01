package com.stem.chatcake.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityMainBinding;
import com.stem.chatcake.service.StorageService;
import com.stem.chatcake.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = MainViewModel.builder()
                .context(this)
                .storageService(StorageService.getInstance(this))
                .build();
        binding.setViewModel(viewModel);
    }
}
