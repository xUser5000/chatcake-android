package com.stem.chatcake.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stem.chatcake.R;
import com.stem.chatcake.databinding.ActivityHomeBinding;
import com.stem.chatcake.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        HomeViewModel viewModel = new HomeViewModel(this, getSupportFragmentManager());
        binding.setViewModel(viewModel);
    }
}