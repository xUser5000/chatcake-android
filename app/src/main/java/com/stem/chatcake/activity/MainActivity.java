package com.stem.chatcake.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityMainBinding;
import com.stem.chatcake.service.LocalStorageService;
import com.stem.chatcake.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // constructing the view model
        viewModel = MainViewModel.builder()
                .host(this)
                .localStorageService(LocalStorageService.getInstance(this))
                .build();

        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.init();
    }
}
